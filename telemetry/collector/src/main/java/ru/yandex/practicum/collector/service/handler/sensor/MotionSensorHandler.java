package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.model.sensorEvent.MotionEvent;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEventType;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.MotionSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class MotionSensorHandler extends BaseSensorEventHandler {
    public MotionSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(SensorEvent sensorEvent) {
        MotionEvent event = (MotionEvent) sensorEvent;

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(new MotionSensorAvro(event.getLinkQuality(), event.getMotion(), event.getVoltage()))
                .build()
                ;
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.MOTION_SENSOR_EVENT;
    }
}
