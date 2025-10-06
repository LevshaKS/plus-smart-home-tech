package ru.pacticum.service.handler.hub;

import ru.pacticum.model.hubEvent.DeviceAddedEvent;
import ru.pacticum.model.hubEvent.HubEvent;
import ru.pacticum.model.hubEvent.DeviceType;
import ru.pacticum.model.hubEvent.HubEventType;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.pacticum.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
public class DeviceAddedHubHandler extends BaseHubEventHandler {
    public DeviceAddedHubHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(HubEvent hubEvent) {
        DeviceAddedEvent event = (DeviceAddedEvent) hubEvent;
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(hubEvent.getTimestamp())
                .setPayload(new DeviceAddedEventAvro(event.getId(), mapToDeviceTypeAvro(event.getDeviceType())))
                .build();
    }

    private DeviceTypeAvro mapToDeviceTypeAvro(DeviceType deviceEventType) {
        DeviceTypeAvro typeAvro = null;
        switch (deviceEventType) {
            case LIGHT_SENSOR -> typeAvro = DeviceTypeAvro.LIGHT_SENSOR;
            case MOTION_SENSOR -> typeAvro = DeviceTypeAvro.MOTION_SENSOR;
            case SWITCH_SENSOR -> typeAvro = DeviceTypeAvro.SWITCH_SENSOR;
            case CLIMATE_SENSOR -> typeAvro = DeviceTypeAvro.CLIMATE_SENSOR;
            case TEMPERATURE_SENSOR -> typeAvro = DeviceTypeAvro.TEMPERATURE_SENSOR;
        }
        return typeAvro;
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.DEVICE_ADDED;
    }
}
