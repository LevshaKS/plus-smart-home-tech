package ru.yandex.practicum.aggregator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.kafka")
public class KafkaPropertiesConfig {
    private Producers producers = new Producers();
    private Consumers consumers = new Consumers();
    private String bootstrapServers;
    private Duration consumerPollTimeout;

    @Getter
    @Setter
    public static class Producers {
        private Map<String, String> properties;
        private String topics;
    }


    @Getter
    @Setter
    public static class Consumers {
        private Map<String, String> properties;
        private List<String> topics;
    }

}
