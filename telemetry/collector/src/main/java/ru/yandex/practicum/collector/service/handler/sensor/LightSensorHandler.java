package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.model.sensorEvent.LightSensorEvent;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEventType;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEvent;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.LightSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class LightSensorHandler extends BaseSensorEventHandler {
    public LightSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(SensorEvent sensorEvent) {
        LightSensorEvent event = (LightSensorEvent) sensorEvent;

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(new LightSensorAvro(event.getLinkQuality(), event.getLuminosity()))
                .build()
                ;
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
