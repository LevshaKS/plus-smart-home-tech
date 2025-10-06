package ru.pacticum.service.handler.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.pacticum.model.hubEvent.HubEvent;
import org.apache.avro.specific.SpecificRecordBase;
import ru.pacticum.producer.KafkaEventProducer;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseHubEventHandler implements HubEventHandler {

    private final KafkaEventProducer producer;

    private String topic = "telemetry.hubs.v1";


    @Override
    public void handler(HubEvent hubEventHandler) {
        log.info("отпрвка в кафку " + hubEventHandler.toString());
        producer.send(toAvro(hubEventHandler), hubEventHandler.getHubId(), hubEventHandler.getTimestamp(), topic);

    }

    public abstract SpecificRecordBase toAvro(HubEvent hubEvent);

}
