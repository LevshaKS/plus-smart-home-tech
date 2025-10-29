package ru.practicum.kafka.deserializer;

import org.apache.avro.Schema;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventProtocol;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

public class SensorSnapshotEventDeserializer extends BaseAvroDeserializer<SensorsSnapshotAvro>{
    public SensorSnapshotEventDeserializer() {
        super(SensorsSnapshotAvro.getClassSchema());
    }
}
