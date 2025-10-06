package ru.yandex.pacticum.model.hubEvent;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class DeviceAction {

    private String sensorId;
    private DeviceActionType type;
    private int value;
}
