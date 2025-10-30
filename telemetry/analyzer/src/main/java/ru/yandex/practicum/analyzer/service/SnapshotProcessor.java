package ru.yandex.practicum.analyzer.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.SnapshotHandler;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.List;


@Component
@Slf4j
@RequiredArgsConstructor

public class SnapshotProcessor implements Runnable {


    private final Consumer<String, SensorsSnapshotAvro> consumer;


    @Value("${spring.kafka.analyzer.consumer.snapshot.pull-timeout}")
    private Duration pollTimeout;


    @Value("${topic.snapshots-topic}")
    private String topics;


    //    private final KafkaConsumer<String, SensorsSnapshotAvro> consumer;
    private final SnapshotHandler snapshotHandler;
//    private final Duration pollTimeout;
//    private final List<String> topics;

//    public SnapshotProcessor(KafkaConfig config, KafkaConsumer<String, HubEventAvro> consumer, SnapshotHandler snapshotHandler, Duration pollTimeout, List<String> topics) {
//
//        this.snapshotHandler = snapshotHandler;
//
//        final KafkaConfig.ConsumerConfig consumerConfig =
//                config.getConsumers().get(this.getClass().getSimpleName());
//
//        this.consumer = new KafkaConsumer<>(consumerConfig.getProperties());
//        this.pollTimeout = consumerConfig.getPollTimeout();
//        this.topics = consumerConfig.getTopics();
//
//        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//            log.info("сработал хук на завершение JVM. перерывается консьюмер");
//            consumer.wakeup();
//        }));
//    }


    @Override
    public void run() {

        try {
            log.info("Подписваемся на топик {}", topics);
            consumer.subscribe(List.of(topics));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("сработал хук на завершение JVM. перерывается консьюмер");
                consumer.wakeup();
            }));
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> records = consumer.poll(pollTimeout);
                for (ConsumerRecord<String, SensorsSnapshotAvro> record : records) {
                    SensorsSnapshotAvro sensorsSnapshot = record.value();
                    log.info("получен снимок {} ", sensorsSnapshot);
                    snapshotHandler.handlerSnapshot(sensorsSnapshot);

                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка получения данных {}", topics);
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}

