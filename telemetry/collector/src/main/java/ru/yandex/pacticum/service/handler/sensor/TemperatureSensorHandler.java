package ru.yandex.pacticum.service.handler.sensor;

import ru.yandex.pacticum.model.sensorEvent.SensorEventType;
import ru.yandex.pacticum.model.sensorEvent.TemperatureSensorEvent;
import ru.yandex.pacticum.producer.KafkaEventProducer;
import ru.yandex.pacticum.model.sensorEvent.SensorEvent;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

@Component
public class TemperatureSensorHandler extends BaseSensorEventHandler {
    public TemperatureSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(SensorEvent sensorEvent) {
        TemperatureSensorEvent event = (TemperatureSensorEvent) sensorEvent;

        return SensorEventAvro.newBuilder()
                .setId(sensorEvent.getId())
                .setHubId(sensorEvent.getHubId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(new TemperatureSensorAvro(event.getTemperatureC(), event.getTemperatureF()))
                .build()
                ;
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.TEMPERATURE_SENSOR_EVENT;
    }
}
