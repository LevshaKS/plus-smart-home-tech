//package ru.yandex.practicum.aggregator.config;
//
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.Setter;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//
//import java.time.Duration;
//import java.util.List;
//import java.util.Map;
//import java.util.Properties;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
//@Getter
//@AllArgsConstructor
//@ConfigurationProperties("aggregator.kafka")
//public class KafkaConfig {
//   private final String bootstrapServers;
//    private final Map<String, ConsumerConfig> consumers;
//    private final Map<String, ProducerConfig> producers;
//
//    public KafkaConfig(String bootstrapServers, Map<String, String> commonProperties, List<ConsumerConfig> consumers, List<ProducerConfig> producers) {
//        this.bootstrapServers = bootstrapServers;
//        this.consumers = consumers.stream()
//                .peek(consumerConfigs -> {
//                    Properties config = new Properties();
//                    config.putAll(commonProperties);
//                    config.putAll(consumerConfigs.getProperties());
//                    consumerConfigs.setProperties(config);
//                })
//                .collect(Collectors.toMap(ConsumerConfig::getType, Function.identity()));
//
//        this.producers = producers.stream()
//                .peek(producerConfig -> {
//                    Properties pConfig = new Properties();
//                    pConfig.putAll(commonProperties);
//                    pConfig.putAll(producerConfig.getProperties());
//                    producerConfig.setProperties(pConfig);
//                })
//                .collect(Collectors.toMap(ProducerConfig::getType, Function.identity()));
//
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
//        public ConsumerConfig(String type, List<String> topics, Duration pollTimeout, Map<String, String> properties) {
//            this.type = type;
//            this.topics = topics;
//            this.pollTimeout = pollTimeout;
//
//            this.properties = new Properties(properties.size());
//        }
//    }
//
//    @Getter
//    @Setter
//    public static class ProducerConfig {
//        private String type;
//        private String topics;
//        private Properties properties;
//
//        public ProducerConfig(String type, String topics, Map<String, String> properties) {
//            this.type = type;
//            this.topics = topics;
//            this.properties = new Properties(properties.size());
//        }
//
//    }
//
//}
