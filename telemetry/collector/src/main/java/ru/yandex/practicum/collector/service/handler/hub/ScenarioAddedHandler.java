package ru.yandex.practicum.collector.service.handler.hub;

import ru.yandex.practicum.collector.model.hubEvent.*;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.util.List;

@Component
public class ScenarioAddedHandler extends BaseHubEventHandler {
    public ScenarioAddedHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(HubEvent hubEvent) {
        ScenarioAddedEvent event = (ScenarioAddedEvent) hubEvent;

        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(new ScenarioAddedEventAvro(event.getName(), mapToScenarioConditionTypeAvro(event.getConditions()),
                        mapToDeviceActionAvro(event.getActions())))
                .build();
    }

    private List<ScenarioConditionAvro> mapToScenarioConditionTypeAvro(List<ScenarioCondition> conditions) {
        List<ScenarioConditionAvro> result = conditions.stream()
                .map(condition -> ScenarioConditionAvro.newBuilder()
                        .setSensorId(condition.getSensorId())
                        .setType(switch (condition.getType()) {
                            case MOTION -> ConditionTypeAvro.MOTION;
                            case LUMINOSITY -> ConditionTypeAvro.LUMINOSITY;
                            case SWITCH -> ConditionTypeAvro.SWITCH;
                            case TEMPERATURE -> ConditionTypeAvro.TEMPERATURE;
                            case CO2LEVEL -> ConditionTypeAvro.CO2LEVEL;
                            case HUMIDITY -> ConditionTypeAvro.HUMIDITY;
                        })
                        .setOperation(switch (condition.getOperation()) {
                                    case EQUALS -> ConditionOperationAvro.EQUALS;
                                    case GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
                                    case LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
                                }

                        )
                        .setValue(condition.getValue())
                        .build())
                .toList();
        return result;
    }

    private List<DeviceActionAvro> mapToDeviceActionAvro(List<DeviceAction> deviceActions) {
        List<DeviceActionAvro> result = deviceActions.stream()
                .map(deviceAction -> DeviceActionAvro.newBuilder()
                        .setSensorId(deviceAction.getSensorId())
                        .setType(switch (deviceAction.getType()) {
                            case ACTIVATE -> ActionTypeAvro.ACTIVATE;
                            case DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
                            case INVERSE -> ActionTypeAvro.INVERSE;
                            case SET_VALUE -> ActionTypeAvro.SET_VALUE;
                        })
                        .setValue(deviceAction.getValue())
                        .build())
                .toList();
        return result;
    }

    @Override
    public HubEventType getMessageType() {
        return HubEventType.SCENARIO_ADDED;
    }
}
