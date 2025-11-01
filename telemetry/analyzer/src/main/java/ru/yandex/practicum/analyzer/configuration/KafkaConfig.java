package ru.yandex.practicum.analyzer.configuration;


import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;


@Configuration


public class KafkaConfig {

    private final KafkaPropertiesConfig config;

    public KafkaConfig(KafkaPropertiesConfig config) {
        this.config = config;
    }

    //консумер хаба
    @Bean
    public KafkaConsumer<String, HubEventAvro> getHubEventProperties() {
        Properties properties = new Properties();
        properties.putAll(config.getConsumers().getHub().getProperties());
        return new KafkaConsumer<>(properties);
    }

    //консумер снапшота
    @Bean
    public KafkaConsumer<String, SensorsSnapshotAvro> getSnapshotProperties() {
        Properties properties = new Properties();
        properties.putAll(config.getConsumers().getSnapshot().getProperties());
        return new KafkaConsumer<>(properties);
    }

}
