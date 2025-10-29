package ru.yandex.practicum.aggregator.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final Environment environment;
    @Bean
    public KafkaProducer<String, SensorsSnapshotAvro> getProducer (){
        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.bootstrap-servers"));
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.key-serializer"));
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.producer.value-serializer"));
        config.put(ProducerConfig.LINGER_MS_CONFIG, environment.getProperty("spring.kafka.producer.linger-ms"));
          return  new KafkaProducer<>(config);
    }
}
