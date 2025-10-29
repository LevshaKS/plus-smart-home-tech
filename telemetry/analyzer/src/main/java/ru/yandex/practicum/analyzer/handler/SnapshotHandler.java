package ru.yandex.practicum.analyzer.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.controller.HubRouterController;
import ru.yandex.practicum.analyzer.model.Conditions;
import ru.yandex.practicum.analyzer.model.Scenarios;
import ru.yandex.practicum.analyzer.repistory.ActionsRepository;
import ru.yandex.practicum.analyzer.repistory.ConditionsRepository;
import ru.yandex.practicum.analyzer.repistory.ScenariosRepository;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SnapshotHandler {
    private final ConditionsRepository conditionRepository;
    private final ScenariosRepository scenarioRepository;
    private final ActionsRepository actionRepository;
    private final HubRouterController hubRouterClient;


    public void handlerSnapshot (SensorsSnapshotAvro sensorsSnapshot){
        Map<String, SensorStateAvro> sensorStateMap =sensorsSnapshot.getSensorsState();
        List<Scenarios> scenarios = scenarioRepository.findByHubId(sensorsSnapshot.getHubId());
        scenarios.stream()
                .filter(scenario -> checkScenario(scenario, sensorStateMap))
                .forEach(scenario -> actionRepository.findAllByScenario(scenario).forEach(hubRouterClient::sendActions) );

    }
    private boolean checkScenario(Scenarios scenario, Map<String, SensorStateAvro> sensorStateMap) {
        List<Conditions> conditions = conditionRepository.findAllByScenario(scenario);
        return conditions.stream().noneMatch(condition -> !checkCondition(condition, sensorStateMap));
    }



    private boolean checkCondition(Conditions condition, Map<String, SensorStateAvro> sensorStateMap) {
        String sensorId = condition.getSensor().getId();
        SensorStateAvro sensorState = sensorStateMap.get(sensorId);
        if (sensorState == null) {
            return false;
        }

        switch (condition.getType()) {
            case LUMINOSITY -> {
                LightSensorAvro lightSensor = (LightSensorAvro) sensorState.getData();
                return checkOperation(condition, lightSensor.getLuminosity());
            }
            case TEMPERATURE -> {
                ClimateSensorAvro temperatureSensor = (ClimateSensorAvro) sensorState.getData();
                return checkOperation(condition, temperatureSensor.getTemperatureC());
            }
            case MOTION -> {
                MotionSensorAvro motionSensor = (MotionSensorAvro) sensorState.getData();
                return checkOperation(condition, motionSensor.getMotion() ? 1 : 0);
            }
            case SWITCH -> {
                SwitchSensorAvro switchSensor = (SwitchSensorAvro) sensorState.getData();
                return checkOperation(condition, switchSensor.getState() ? 1 : 0);
            }
            case CO2LEVEL -> {
                ClimateSensorAvro climateSensor = (ClimateSensorAvro) sensorState.getData();
                return checkOperation(condition, climateSensor.getCo2Level());
            }
            case HUMIDITY -> {
                ClimateSensorAvro climateSensor = (ClimateSensorAvro) sensorState.getData();
                return checkOperation(condition, climateSensor.getHumidity());
            }
            case null -> {
                return false;
            }
        }
    }


        private Boolean checkOperation(Conditions condition, Integer currentValue){
            ConditionOperationAvro conditionOperation = condition.getOperation();
            Integer targetValue = condition.getValue();

            switch (conditionOperation) {
                case EQUALS -> {
                    return targetValue == currentValue;
                }
                case LOWER_THAN -> {
                    return currentValue < targetValue;
                }
                case GREATER_THAN -> {
                    return currentValue > targetValue;
                }
                case null -> {
                    return null;
                }
            }
        }
    }