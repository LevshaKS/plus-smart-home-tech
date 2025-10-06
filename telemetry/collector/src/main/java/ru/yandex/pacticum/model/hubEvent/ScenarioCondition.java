package ru.yandex.pacticum.model.hubEvent;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ScenarioCondition {

    private String sensorId;
    private ScenarioConditionType type;
    private ScenarioConditionOperationType operation;
    private int value;
}
