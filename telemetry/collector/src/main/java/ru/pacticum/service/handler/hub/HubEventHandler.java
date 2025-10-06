package ru.pacticum.service.handler.hub;

import ru.pacticum.model.hubEvent.HubEvent;
import ru.pacticum.model.hubEvent.HubEventType;

public interface HubEventHandler {

    HubEventType getMessageType();

    void handler(HubEvent hubEvent);
}
