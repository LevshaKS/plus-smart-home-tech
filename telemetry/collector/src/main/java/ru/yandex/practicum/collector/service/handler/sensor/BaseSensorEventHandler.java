package ru.yandex.practicum.collector.service.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEvent;
import org.apache.avro.specific.SpecificRecordBase;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler implements SensorEventHandler {

    private final KafkaEventProducer producer;

    private final String topic = "telemetry.sensors.v1";

    @Override
    public void handler(SensorEvent sensorEventHandler) {
        log.info("отпрвка в кафку " + sensorEventHandler.toString());
        producer.send(toAvro(sensorEventHandler), sensorEventHandler.getHubId(), sensorEventHandler.getTimestamp(), topic);

    }

    public abstract SpecificRecordBase toAvro(SensorEvent sensorEvent);

}
