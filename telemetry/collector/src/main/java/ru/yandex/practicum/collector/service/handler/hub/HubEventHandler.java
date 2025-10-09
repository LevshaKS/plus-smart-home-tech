package ru.yandex.practicum.collector.service.handler.hub;

import ru.yandex.practicum.collector.model.hubEvent.HubEvent;
import ru.yandex.practicum.collector.model.hubEvent.HubEventType;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handler(HubEvent hubEvent);
}
