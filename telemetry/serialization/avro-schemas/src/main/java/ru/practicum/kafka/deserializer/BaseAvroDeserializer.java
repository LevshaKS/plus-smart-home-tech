package ru.practicum.kafka.deserializer;

import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
public class BaseAvroDeserializer <T extends SpecificRecordBase> implements Deserializer<T> {
    private final DecoderFactory decoderFactory;
    private final DatumReader<T> reader;

    public BaseAvroDeserializer(DecoderFactory decoderFactory, Schema schema) {
        this.decoderFactory = decoderFactory;
        this.reader = new SpecificDatumReader<>(schema);
    }

    public BaseAvroDeserializer(Schema schema) {
        this.decoderFactory = DecoderFactory.get();
        this.reader = new SpecificDatumReader<>(schema);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            if (data != null) {
                BinaryDecoder decoder = decoderFactory.binaryDecoder(data, null);
                log.info("Десереализация данных из топика [" + topic + "]");
                return this.reader.read(null, decoder);
            }
            return null;
        } catch (Exception e) {
            log.warn("Ошибка десереализации данных из топика [" + topic + "]");
            throw new DeserializationException("Ошибка десереализации данных из топика [" + topic + "]", e);
        }
    }
}
