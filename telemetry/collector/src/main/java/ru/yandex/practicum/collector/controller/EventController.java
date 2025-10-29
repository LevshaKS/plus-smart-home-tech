package ru.yandex.practicum.collector.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import net.devh.boot.grpc.server.service.GrpcService;

import ru.yandex.practicum.collector.handler.hub.HubEventHandler;
import ru.yandex.practicum.collector.handler.sensor.SensorEventHandler;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.CollectorResponse;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@GrpcService
public class EventController extends CollectorControllerGrpc.CollectorControllerImplBase {
    private final Map<SensorEventProto.PayloadCase, SensorEventHandler> sensorEventHandlers;

    private final Map<HubEventProto.PayloadCase, HubEventHandler> hubEventHandlers;

    public EventController(Set<SensorEventHandler> sensorEventHandlers, Set<HubEventHandler> hubEventHandlers) {
        this.sensorEventHandlers = sensorEventHandlers.stream().collect(Collectors.toMap(SensorEventHandler::getMessageType, Function.identity()));
        this.hubEventHandlers = hubEventHandlers.stream().collect(Collectors.toMap(HubEventHandler::getMessageType, Function.identity()));
    }

    @Override
    public void collectSensorEvent(SensorEventProto request, StreamObserver<CollectorResponse> responseObserver) {  //request - событие от датчика   //responceObserver - ответ для клиента
        try { //проверяем если ли обраотчик для полученого события
            if (sensorEventHandlers.containsKey(request.getPayloadCase())) {
                log.info("обработчик найден " + request.getPayloadCase());
                sensorEventHandlers.get(request.getPayloadCase()).handler(request);  //если обработчик есть то передаем события на обработку
            } else {
                log.warn("нет обработчика для собыитя " + request.getPayloadCase());
                throw new IllegalArgumentException("нет обработчика для собыитя" + request.getPayloadCase());
            }
            responseObserver.onNext(CollectorResponse.getDefaultInstance()); //после обраотки события возвращаем ответ клиенту
            responseObserver.onCompleted();  //завершаем обработку запроса
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));  //в случаи ошибки отправляем ошибку клиенту
        }
    }

    @Override
    public void collectHubEvent(HubEventProto request, StreamObserver<CollectorResponse> responseObserver) {
        try {
            if (hubEventHandlers.containsKey(request.getPayloadCase())) {
                log.info("обработчик найден " + request.getPayloadCase());
                hubEventHandlers.get(request.getPayloadCase()).handler(request);
            } else {
                log.warn("нет обработчика для собыитя " + request.getPayloadCase());
                throw new IllegalArgumentException("нет обработчика для собыитя" + request.getPayloadCase());
            }
            responseObserver.onNext(CollectorResponse.getDefaultInstance()); //после обраотки события возвращаем ответ клиенту
            responseObserver.onCompleted();  //завершаем обработку запроса
        } catch (Exception e) {
            responseObserver.onError(new StatusRuntimeException(Status.fromThrowable(e)));  //в случаи ошибки отправляем ошибку клиенту
        }
    }

}
