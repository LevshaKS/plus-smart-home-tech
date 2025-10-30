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

import ru.yandex.practicum.analyzer.handler.HubHandler;
import ru.yandex.practicum.analyzer.handler.HubHandlers;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Duration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;


@Component
@Slf4j
@RequiredArgsConstructor

public class HubEventProcessor implements Runnable {

    // private final KafkaConsumer<String, HubEventAvro> consumer;

    private final Consumer<String, HubEventAvro> consumer;
    private final HubHandlers hubHandlers;


    @Value("${spring.kafka.analyzer.consumer.hub.pull-timeout}")
    private Duration pollTimeout;
    //private final List<String> topics;

    @Value("${topic.hub-event-topic}")
    private String topics;

//    public HubEventProcessor ( Set<HubHandler> hubHandlers, Consumer<String, HubEventAvro> consumer){
//        this.hubHandlers = hubHandlers.stream().collect(Collectors.toMap(HubHandler::getMessageType, Function.identity()));
//        this.consumer=consumer;
//    }

//    public HubEventProcessor(KafkaConfig config, KafkaConsumer<String, HubEventAvro> consumer, Set<HubHandler> hubHandlers, Duration pollTimeout, List<String> topics) {
//        this.hubHandlers = hubHandlers.stream().collect(Collectors.toMap(HubHandler::getMessageType, Function.identity()));
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
        Map<String, HubHandler> hubHandlerMap = hubHandlers.getHandlers();

        try {
            log.info("Подписваемся на топик {}", topics);
            consumer.subscribe(List.of(topics));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                log.info("сработал хук на завершение JVM. перерывается консьюмер");
                consumer.wakeup();
            }));

            while (true) {
                ConsumerRecords<String, HubEventAvro> records = consumer.poll(pollTimeout);
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