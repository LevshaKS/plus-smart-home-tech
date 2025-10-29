package ru.yandex.practicum.analyzer.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.configuration.KafkaConfig;
import ru.yandex.practicum.analyzer.handler.HubHandler;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j

public class HubEventProcessor implements Runnable {

    private final KafkaConsumer<String, HubEventAvro> consumer;
    private final Map<String, HubHandler> hubHandlers;

    private final Duration pollTimeout;
    private final List<String> topics;

    public HubEventProcessor(KafkaConfig config, KafkaConsumer<String, HubEventAvro> consumer, Set<HubHandler> hubHandlers , Duration pollTimeout, List<String> topics) {
        this.hubHandlers = hubHandlers.stream().collect(Collectors.toMap(HubHandler::getMessageType, Function.identity()));

       final KafkaConfig.ConsumerConfig consumerConfig =
       config.getConsumers().get(this.getClass().getSimpleName());

        this.consumer = new KafkaConsumer<>(consumerConfig.getProperties());
        this.pollTimeout = consumerConfig.getPollTimeout();
        this.topics = consumerConfig.getTopics();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            log.info("сработал хук на завершение JVM. перерывается консьюмер");
            consumer.wakeup();
    }));
    }



    @Override
    public void run() {
        log.info("Подписваемся на топик {}", topics);
        consumer.subscribe(topics);
        try {
            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(pollTimeout);
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro hubEvent = record.value();
                    String payload =hubEvent.getClass().getSimpleName();

                    if (hubHandlers.containsKey(payload)){
                    hubHandlers.get(payload).handler(hubEvent);
                    log.info("получен хаба {} ", hubEvent);
                    }
                 else {
                        log.info("нет обработчика для события {} ", hubEvent);
                 }
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