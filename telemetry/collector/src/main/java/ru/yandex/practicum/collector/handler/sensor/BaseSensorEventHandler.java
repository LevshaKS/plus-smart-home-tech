package ru.yandex.practicum.collector.handler.sensor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseSensorEventHandler implements SensorEventHandler {

    private final KafkaEventProducer producer;

    private final String topic = "telemetry.sensors.v1";

    @Override
    public void handler(SensorEventProto sensorEventHandler) {
        log.info("отпрвка в кафку {} ", sensorEventHandler.toString());
        producer.send(toAvro(sensorEventHandler), sensorEventHandler.getHubId(),

                Instant.ofEpochSecond(sensorEventHandler.getTimestamp().getSeconds(), sensorEventHandler.getTimestamp().getNanos()), topic);

    }

    public abstract SpecificRecordBase toAvro(SensorEventProto sensorEvent);

}
