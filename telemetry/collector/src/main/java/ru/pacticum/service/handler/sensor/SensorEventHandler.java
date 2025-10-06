package ru.pacticum.service.handler.sensor;

import ru.pacticum.model.sensorEvent.SensorEvent;
import ru.pacticum.model.sensorEvent.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getMessageType();

    void handler(SensorEvent sensorEventHandler);
}
