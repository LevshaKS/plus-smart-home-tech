package ru.yandex.practicum.aggregator.config;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration

public class KafkaConfig {


    private final KafkaPropertiesConfig config;

    public KafkaConfig(KafkaPropertiesConfig config) {
        this.config = config;
    }


    @Bean
    public KafkaProducer<String, SensorsSnapshotAvro> getProducer() {
        Properties properties = new Properties();
        properties.putAll(config.getProducers().getProperties());

        return new KafkaProducer<>(properties);
    }


    @Bean
    public KafkaConsumer<String, SensorEventAvro> getConsumer() {
        Properties properties = new Properties();
        properties.putAll(config.getConsumers().getProperties());

        return new KafkaConsumer<>(properties);
    }
}
