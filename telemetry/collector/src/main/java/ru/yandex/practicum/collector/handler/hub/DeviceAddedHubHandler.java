package ru.yandex.practicum.collector.handler.hub;


import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.DeviceTypeAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

import java.time.Instant;

@Component
public class DeviceAddedHubHandler extends BaseHubEventHandler {
    public DeviceAddedHubHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(HubEventProto hubEvent) {
        DeviceAddedEventProto event = hubEvent.getDeviceAdded();
        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(hubEvent.getTimestamp().getSeconds(), hubEvent.getTimestamp().getNanos()))
                .setPayload(new DeviceAddedEventAvro(event.getId(), mapToDeviceTypeAvro(event.getType())))
                .build();
    }

    private DeviceTypeAvro mapToDeviceTypeAvro(DeviceTypeProto deviceEventType) {
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
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.DEVICE_ADDED;
    }
}
