package ru.yandex.practicum.aggregator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;

//import ru.yandex.practicum.aggregator.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

//        private final KafkaProducer<String, SensorsSnapshotAvro> producer;
//    private  final KafkaConsumer<String, SensorEventAvro> consumer;
//    private final SnapshotService snapshotService;
//    private final List<String> topicsConsumer;
//    private final Duration pollTimeout;
//    private final String topicsProducer;

//    public AggregationStarter (SnapshotService snapshotService, KafkaConfig config, List<String> topics, Duration pollTimeout, List<String> topicsProducer){
//        this.snapshotService =snapshotService;
//
//final  KafkaConfig.ProducerConfig producerConfig =
//        config.getProducers().get(this.getClass().getSimpleName());
//
//        final KafkaConfig.ConsumerConfig consumerConfig =
//                config.getConsumers().get(this.getClass().getSimpleName());
//
//        this.consumer= new KafkaConsumer<>(consumerConfig.getProperties());
//        this.topicsConsumer = consumerConfig.getTopics();
//        this.pollTimeout = consumerConfig.getPollTimeout();
//
//        this.topicsProducer =producerConfig.getTopics();
//        this.producer = new KafkaProducer<>(producerConfig.getProperties());
//
//
//
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            log.info("сработал хук на завершение JVM. перерывается консьюмер");
//            consumer.wakeup();
//        }));
//
//    }

    // ... объявление полей и конструктора ...
    private final Producer<String, SensorsSnapshotAvro> producer;
    private final Consumer<String, SensorEventAvro> consumer;
    private final SnapshotService snapshotService;

    // ... объявление полей и конструктора ...
//    private final Producer<String, SensorsSnapshotAvro> producer;
//    private final Consumer<String, SensorEventAvro> consumer;


    private String topicTelemetrySensors = "telemetry.sensors.v1";


    private String topicTelemetrySnapshots = "telemetry.snapshots.v1";

    public void start() {
        try {
            consumer.subscribe(List.of(topicTelemetrySensors));
            //     ... подготовка к обработке данных ...
            //     ... например, подписка на топик ...
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("сработал хук на завершение JVM. перерывается консьюмер");
                consumer.wakeup();

                //  consumer.subscribe(topicsConsumer);
            }));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, SensorEventAvro> record : records) {

                    SensorEventAvro sensorEventAvro = record.value();
                    log.info("обработка входящих данных " + sensorEventAvro);
                    Optional<SensorsSnapshotAvro> snapshotAvro = snapshotService.updateState(sensorEventAvro);
                    log.info("Получение снимка " + snapshotAvro);
                    if (snapshotAvro.isPresent()) {
                        log.info("отпраляем список в топик кафки");
                        ProducerRecord<String, SensorsSnapshotAvro> message = new ProducerRecord<>(topicTelemetrySnapshots, null,
                                sensorEventAvro.getTimestamp().toEpochMilli(), sensorEventAvro.getHubId(), snapshotAvro.get());

                        producer.send(message);
                    }

                }
                consumer.commitSync();
                // ... реализация цикла опроса ...
                // ... и обработка полученных данных ...
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

                // здесь нужно вызвать метод продюсера для сброса данных в буффере
                // здесь нужно вызвать метод консьюмера для фиксиции смещений

            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }
}