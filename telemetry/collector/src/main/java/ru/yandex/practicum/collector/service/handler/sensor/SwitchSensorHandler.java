package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.model.sensorEvent.SensorEventType;
import ru.yandex.practicum.collector.model.sensorEvent.SwitchSensorEvent;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEvent;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SwitchSensorAvro;

@Component
public class SwitchSensorHandler extends BaseSensorEventHandler {
    public SwitchSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(SensorEvent sensorEvent) {
        SwitchSensorEvent event = (SwitchSensorEvent) sensorEvent;

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(new SwitchSensorAvro(event.getState()))
                .build()
                ;
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.SWITCH_SENSOR_EVENT;
    }
}
