package ru.pacticum.service.handler.sensor;

import ru.pacticum.model.sensorEvent.ClimateSensorEvent;
import ru.pacticum.model.sensorEvent.SensorEventType;
import ru.pacticum.producer.KafkaEventProducer;
import ru.pacticum.model.sensorEvent.SensorEvent;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.ClimateSensorAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;

@Component
public class ClimateSensorHandler extends BaseSensorEventHandler {
    public ClimateSensorHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(SensorEvent sensorEvent) {
        ClimateSensorEvent event = (ClimateSensorEvent) sensorEvent;
        return SensorEventAvro.newBuilder()
                .setHubId(sensorEvent.getHubId())
                .setId(sensorEvent.getId())
                .setTimestamp(sensorEvent.getTimestamp())
                .setPayload(new ClimateSensorAvro(event.getTemperatureC(), event.getHumidity(), event.getCo2Level()))
                .build()
                ;
    }

    @Override
    public SensorEventType getMessageType() {
        return SensorEventType.CLIMATE_SENSOR_EVENT;
    }
}
