package ru.yandex.practicum.collector.service.handler.sensor;

import ru.yandex.practicum.collector.model.sensorEvent.SensorEvent;
import ru.yandex.practicum.collector.model.sensorEvent.SensorEventType;

public interface SensorEventHandler {

    SensorEventType getMessageType();

    void handler(SensorEvent sensorEventHandler);
}
