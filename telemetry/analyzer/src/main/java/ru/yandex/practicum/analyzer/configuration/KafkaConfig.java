package ru.yandex.practicum.analyzer.configuration;

import lombok.RequiredArgsConstructor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;
import java.util.Properties;


//@Getter
@Configuration
@RequiredArgsConstructor
//@ConfigurationProperties(prefix = "analyzer.kafka")
public class KafkaConfig {
//    private final Map<String, ConsumerConfig> consumers;
//
//
//    public KafkaConfig(Map<String, String> commonProperties, List<ConsumerConfig> consumers) {
//        commonProperties.put("bootstrap-servers", "localhost:9092");
//        commonProperties.put("enable.auto.commit", "false");
//        commonProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//
//        Map <String, String> prop1=new HashMap<>();
//        Map <String, String> prop2=new HashMap<>();
//
//        prop1.put("value.deserializer","ru.practicum.kafka.deserializer.SnapshotDeserializer" );
//        prop1.put("group.id","telemetry.analyzer.snapshot");
//
//        prop2.put("value.deserializer", "ru.practicum.kafka.deserializer.HubEventDeserializer");
//        prop2.put( "group.id", "telemetry.analyzer.hub");
//
//        ConsumerConfig config1 = new ConsumerConfig("SnapshotProcessor",List.of("telemetry.snapshots.v1"),Duration.ofMillis(500),  prop1);
//        ConsumerConfig config2 = new ConsumerConfig("HubEventProcessor",List.of("telemetry.snapshots.hub"),Duration.ofMillis(2000),  prop2);
//        consumers.add(config1);
//        consumers.add(config2);
//
//
//        this.consumers = consumers.stream()
//                .peek(configs -> {
//                    Properties config = new Properties();
//                    config.putAll(commonProperties);
//                    config.putAll(configs.getProperties());
//                    configs.setProperties(config);
//                })
//                .collect(Collectors.toMap(ConsumerConfig::getType, Function.identity()));
//    }
//
//    @Getter
//    @Setter
//    public static class ConsumerConfig {
//        private String type;
//        private List<String> topics;
//        private Duration pollTimeout;
//        private Properties properties;
//
//
//        public ConsumerConfig(String type, List<String> topics, Duration pollTimeout, Map<String, String> properties) {
//            this.type = type;
//            this.topics = topics;
//            this.pollTimeout = pollTimeout;
//
//            this.properties = new Properties(properties.size());
//            this.properties.putAll(properties);
//
//        }
//    }

    private final Environment environment;
//консумер хаба
    @Bean
    public KafkaConsumer <String, HubEventAvro> getHubEventProperties (){
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.analyzer.bootstrap-servers"));
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.hub.client-id"));
        config.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.hub.group-id"));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.hub.key-deserializer"));
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.hub.value-deserializer"));
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, environment.getProperty("spring.kafka.analyzer.enable-auto-commit"));
        return new KafkaConsumer<>(config);
    }

    //консумер снапшота
    @Bean
    public KafkaConsumer <String, SensorsSnapshotAvro> getSnapshotProperties (){
        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, environment.getProperty("spring.kafka.analyzer.bootstrap-servers"));
        config.put(ConsumerConfig.CLIENT_ID_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.snapshot.client-id"));
        config.put(ConsumerConfig.GROUP_ID_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.snapshot.group-id"));
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.snapshot.key-deserializer"));
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, environment.getProperty("spring.kafka.analyzer.consumer.snapshot.value-deserializer"));
        config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, environment.getProperty("spring.kafka.analyzer.enable-auto-commit"));
        return new KafkaConsumer<>(config);
    }

}
