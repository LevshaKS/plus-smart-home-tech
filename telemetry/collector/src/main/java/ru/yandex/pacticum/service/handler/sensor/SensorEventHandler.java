package ru.yandex.pacticum.service.handler.sensor;

import ru.yandex.pacticum.model.sensorEvent.SensorEvent;
import ru.yandex.pacticum.model.sensorEvent.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getMessageType();

    void handler(SensorEvent sensorEventHandler);
}
