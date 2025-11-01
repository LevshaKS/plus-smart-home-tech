package ru.yandex.practicum.analyzer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import org.apache.kafka.common.errors.WakeupException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.configuration.KafkaPropertiesConfig;
import ru.yandex.practicum.analyzer.handler.HubHandler;
import ru.yandex.practicum.analyzer.handler.HubHandlers;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.util.Map;


@Component
@Slf4j
@RequiredArgsConstructor

public class HubEventProcessor implements Runnable {

    private final KafkaPropertiesConfig config;

    private final Consumer<String, HubEventAvro> consumer;
    private final HubHandlers hubHandlers;


    @Override
    public void run() {
        Map<String, HubHandler> hubHandlerMap = hubHandlers.getHandlers();

        try {
            log.info("Подписваемся на топик {}", config.getConsumers().getHub().getTopics());
            consumer.subscribe(config.getConsumers().getHub().getTopics());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("сработал хук на завершение JVM. перерывается консьюмер");
                consumer.wakeup();
            }));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(config.getHubPollTimeout());
                for (ConsumerRecord<String, HubEventAvro> record : records) {
                    HubEventAvro hubEvent = record.value();
                    String payload = hubEvent.getPayload().getClass().getSimpleName();

                    if (hubHandlerMap.containsKey(payload)) {
                        log.info("получен хаба {} ", hubEvent);
                        hubHandlerMap.get(payload).handler(hubEvent);
                    } else {
                        log.info("нет обработчика для события {} ", hubEvent);
                    }
                }
                consumer.commitAsync();
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка получения данных {}", config.getConsumers().getHub().getTopics());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }
}