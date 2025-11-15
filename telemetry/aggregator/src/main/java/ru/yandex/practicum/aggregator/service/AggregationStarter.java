package ru.yandex.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;


import ru.yandex.practicum.aggregator.config.KafkaPropertiesConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {


    // ... объявление полей и конструктора ...
    private final Producer<String, SensorsSnapshotAvro> producer;
    private final Consumer<String, SensorEventAvro> consumer;
    private final SnapshotService snapshotService;
    private final KafkaPropertiesConfig config;


    public void start() {
        try {
            consumer.subscribe(config.getConsumers().getTopics());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("сработал хук на завершение JVM. перерывается консьюмер");
                consumer.wakeup();


            }));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(config.getConsumerPollTimeout());

                for (ConsumerRecord<String, SensorEventAvro> record : records) {

                    SensorEventAvro sensorEventAvro = record.value();
                    log.info("обработка входящих данных {} ", sensorEventAvro);
                    Optional<SensorsSnapshotAvro> snapshotAvro = snapshotService.updateState(sensorEventAvro);
                    log.info("Получение снимка " + snapshotAvro);
                    if (snapshotAvro.isPresent()) {
                        log.info("отпраляем список в топик кафки");
                        ProducerRecord<String, SensorsSnapshotAvro> message = new ProducerRecord<>(config.getProducers().getTopics(), null,
                                sensorEventAvro.getTimestamp().toEpochMilli(), sensorEventAvro.getHubId(), snapshotAvro.get());

                        producer.send(message);
                    }

                }
                consumer.commitSync();
            }

        } catch (WakeupException ignored) {
            // игнорируем - закрываем консьюмер и продюсер в блоке finally
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                producer.flush();
                consumer.commitSync();
                // Перед тем, как закрыть продюсер и консьюмер, нужно убедится,
                // что все сообщения, лежащие в буффере, отправлены и
                // все оффсеты обработанных сообщений зафиксированы


            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }
}