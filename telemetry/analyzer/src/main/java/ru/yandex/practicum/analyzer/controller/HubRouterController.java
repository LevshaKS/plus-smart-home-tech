package ru.yandex.practicum.analyzer.controller;

import com.google.protobuf.Timestamp;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.analyzer.model.Actions;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.grpc.telemetry.hubrouter.HubRouterControllerGrpc;


import java.time.Instant;

@Service
@Slf4j

public class HubRouterController {
    private final HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient;

    public HubRouterController(@GrpcClient("hub-routers") HubRouterControllerGrpc.HubRouterControllerBlockingStub hubRouterClient) {
        this.hubRouterClient = hubRouterClient;
    }

    public void sendActions(Actions actions) {
        Instant instant = Instant.now();
        DeviceActionRequest deviceActionRequest = DeviceActionRequest.newBuilder()
                .setHubId(actions.getScenario().getHubId())
                .setScenario(actions.getScenario().getName())
                .setAction(DeviceActionProto.newBuilder()
                        .setSensorId(actions.getSensor().getId())
                        .setType(ActionTypeProto.valueOf(String.valueOf(actions.getType())))
                        .setValue(actions.getValue())
                        .build())
                .setTimestamp(Timestamp.newBuilder().setSeconds(instant.getEpochSecond()).setNanos(instant.getNano()))
                .build();
        log.info("отправлено в хаб роутер действие {}", deviceActionRequest);
        hubRouterClient.handleDeviceAction(deviceActionRequest);
    }

}
