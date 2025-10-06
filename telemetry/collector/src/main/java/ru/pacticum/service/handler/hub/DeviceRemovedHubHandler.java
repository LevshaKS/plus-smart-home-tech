package ru.pacticum.service.handler.hub;

import ru.pacticum.model.hubEvent.DeviceRemovedEvent;
import ru.pacticum.model.hubEvent.HubEvent;
import ru.pacticum.model.hubEvent.HubEventType;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.pacticum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceRemovedHubHandler extends BaseHubEventHandler {
    public DeviceRemovedHubHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(HubEvent hubEvent) {
        DeviceRemovedEvent event = (DeviceRemovedEvent) hubEvent;

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(new DeviceRemovedEventAvro(event.getId()))
                .build();
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
