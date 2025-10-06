package ru.yandex.pacticum.model.hubEvent;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;

//запись, описывающая событие добавления сценария.
@Getter
@ToString
public class ScenarioAddedEvent extends HubEvent {

    @NotBlank
    private String name;

    @NotEmpty
    private ArrayList<ScenarioCondition> conditions;
    @NotEmpty
    private ArrayList<DeviceAction> actions;

    @Override
    public HubEventType getType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
