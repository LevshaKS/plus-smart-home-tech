package ru.yandex.pacticum.model.hubEvent;


import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

//событие удаление в хабе
@Getter
@ToString
public class DeviceRemovedEvent extends HubEvent {

    @NotNull
    private String id;

    @Override
    public HubEventType getType() {
        return HubEventType.DEVICE_REMOVED;
    }
}
