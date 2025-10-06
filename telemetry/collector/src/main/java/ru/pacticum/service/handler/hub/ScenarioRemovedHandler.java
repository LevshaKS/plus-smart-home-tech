package ru.pacticum.service.handler.hub;

import ru.pacticum.model.hubEvent.HubEvent;
import ru.pacticum.model.hubEvent.HubEventType;
import ru.pacticum.model.hubEvent.ScenarioRemovedEvent;
import ru.pacticum.producer.KafkaEventProducer;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

@Component
public class ScenarioRemovedHandler extends BaseHubEventHandler {

    public ScenarioRemovedHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(HubEvent hubEvent) {
        ScenarioRemovedEvent event = (ScenarioRemovedEvent) hubEvent;

        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(new ScenarioRemovedEventAvro(event.getName()))
                .build()
                ;
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_REMOVED;
    }
}
