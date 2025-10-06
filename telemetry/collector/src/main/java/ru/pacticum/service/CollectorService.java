package ru.pacticum.service;


import lombok.extern.slf4j.Slf4j;
import ru.pacticum.model.hubEvent.HubEvent;
import ru.pacticum.model.hubEvent.HubEventType;
import ru.pacticum.model.sensorEvent.SensorEventType;
import ru.pacticum.service.handler.hub.HubEventHandler;
import ru.pacticum.service.handler.sensor.SensorEventHandler;
import ru.pacticum.model.sensorEvent.SensorEvent;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service

public class CollectorService {
    private final Map<SensorEventType, SensorEventHandler> sensorEventHandlers;
    private final Map<HubEventType, HubEventHandler> hubEventHandlers;

    public CollectorService(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream()
                .collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream()
                .collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));

    }

    public void collectSensorEvent(SensorEvent sensorEvent) {
        SensorEventHandler sensorEventHandler = sensorEventHandlers.get(sensorEvent.getType());
        if (sensorEventHandler == null) {
            throw new IllegalArgumentException("нет такого обработчика событий " + sensorEvent.getType());
        }
        log.info("обработка события " + sensorEvent.getType());
        sensorEventHandler.handler(sensorEvent);
    }


    public void collectHubEvent(HubEvent hubEvent) {
        HubEventHandler hubEventHandler = hubEventHandlers.get(hubEvent.getType());
        if (hubEventHandler == null) {
            throw new IllegalArgumentException("нет такого обработчика событий " + hubEvent.getType());
        }
        log.info("обработка события " + hubEvent.getType());
        hubEventHandler.handler(hubEvent);
    }

}
