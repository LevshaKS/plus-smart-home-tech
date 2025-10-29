package ru.yandex.practicum.analyzer.handler;


import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubHandler {

    String getMessageType();

    void handler(HubEventAvro hubEvent);
}
