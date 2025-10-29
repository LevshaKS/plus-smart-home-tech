package ru.yandex.practicum.collector.handler.hub;

import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.collector.producer.KafkaEventProducer;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioAddedEventProto;
import ru.yandex.practicum.grpc.telemetry.event.ScenarioConditionProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;
import java.util.List;

@Component
public class ScenarioAddedHandler extends BaseHubEventHandler {
    public ScenarioAddedHandler(KafkaEventProducer producer) {
        super(producer);
    }

    @Override
    public SpecificRecordBase toAvro(HubEventProto hubEvent) {
        ScenarioAddedEventProto event = hubEvent.getScenarioAdded();

        return HubEventAvro.newBuilder()
                .setHubId(hubEvent.getHubId())
                .setTimestamp(Instant.ofEpochSecond(hubEvent.getTimestamp().getSeconds(), hubEvent.getTimestamp().getNanos()))
                .setPayload(new ScenarioAddedEventAvro(event.getName(), mapToScenarioConditionTypeAvro(event.getConditionList()),
                        mapToDeviceActionAvro(event.getActionList())))
                .build();
    }

    private List<ScenarioConditionAvro> mapToScenarioConditionTypeAvro(List<ScenarioConditionProto> conditions) {
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
                            case UNRECOGNIZED -> null;
                        })
                        .setOperation(switch (condition.getOperation()) {
                                    case EQUALS -> ConditionOperationAvro.EQUALS;
                                    case GREATER_THAN -> ConditionOperationAvro.GREATER_THAN;
                                    case LOWER_THAN -> ConditionOperationAvro.LOWER_THAN;
                                    case UNRECOGNIZED -> null;
                                }

                        )
                        .setValue( switch (condition.getValueCase()){
                            case BOOL_VALUE -> condition.getBoolValue();
                            case INT_VALUE -> condition.getIntValue();
                            case VALUE_NOT_SET -> null;
                        })
                        .build())
                .toList();
        return result;
    }

    private List<DeviceActionAvro> mapToDeviceActionAvro(List<DeviceActionProto> deviceActions) {
        List<DeviceActionAvro> result = deviceActions.stream()
                .map(deviceAction -> DeviceActionAvro.newBuilder()
                        .setSensorId(deviceAction.getSensorId())
                        .setType(switch (deviceAction.getType()) {
                            case ACTIVATE -> ActionTypeAvro.ACTIVATE;
                            case DEACTIVATE -> ActionTypeAvro.DEACTIVATE;
                            case INVERSE -> ActionTypeAvro.INVERSE;
                            case SET_VALUE -> ActionTypeAvro.SET_VALUE;
                            case UNRECOGNIZED -> null;
                        })
                        .setValue(deviceAction.getValue())
                        .build())
                .toList();
        return result;
    }

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }
}
