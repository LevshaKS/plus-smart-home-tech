package ru.practicum.kafka.deserializer;

import org.apache.avro.Schema;
import org.apache.avro.io.DecoderFactory;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public class HubEventDeserializer extends  BaseAvroDeserializer<HubEventAvro> {

    public HubEventDeserializer() {
        super(HubEventAvro.getClassSchema());
    }
}
