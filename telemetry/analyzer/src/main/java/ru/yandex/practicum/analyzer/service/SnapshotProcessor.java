package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.configuration.KafkaPropertiesConfig;
import ru.yandex.practicum.analyzer.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;


@Component
@Slf4j
@RequiredArgsConstructor

public class SnapshotProcessor implements Runnable {


    private final Consumer<String, SensorsSnapshotAvro> consumer;

    private final KafkaPropertiesConfig config;


    private final SnapshotHandler snapshotHandler;


    @Override
    public void run() {

        try {
            log.info("Подписваемся на топик {}", config.getConsumers().getSnapshot().getTopics());
            consumer.subscribe(config.getConsumers().getSnapshot().getTopics());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("сработал хук на завершение JVM. перерывается консьюмер");
                consumer.wakeup();
            }));
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(config.getSnapshotPollTimeout());
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    SensorsSnapshotAvro sensorsSnapshot = record.value();
                    log.info("получен снимок {} ", sensorsSnapshot);
                    snapshotHandler.handlerSnapshot(sensorsSnapshot);

                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка получения данных {}", config.getConsumers().getSnapshot().getTopics());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}

