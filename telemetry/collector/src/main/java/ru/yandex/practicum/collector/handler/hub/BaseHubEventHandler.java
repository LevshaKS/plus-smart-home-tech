package ru.yandex.practicum.collector.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.avro.specific.SpecificRecordBase;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;

import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler implements HubEventHandler {

    private final KafkaEventProducer producer;

    private final String topic = "telemetry.hubs.v1";


    @Override
    public void handler(HubEventProto hubEventHandler) {
        log.info("отпрвка в кафку " + hubEventHandler.toString());
        producer.send(toAvro(hubEventHandler), hubEventHandler.getHubId(), Instant.ofEpochSecond(hubEventHandler.getTimestamp().getSeconds(), hubEventHandler.getTimestamp().getNanos()), topic);

    }

    public abstract SpecificRecordBase toAvro(HubEventProto hubEvent);

}
