package ru.yandex.pacticum.service.handler.hub;

import ru.yandex.pacticum.model.hubEvent.HubEvent;
import ru.yandex.pacticum.model.hubEvent.HubEventType;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handler(HubEvent hubEvent);
}
