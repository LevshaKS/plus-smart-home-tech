package ru.yandex.practicum;

import com.google.protobuf.Timestamp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import net.devh.boot.grpc.client.inject.GrpcClient;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;

import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.model.*;

import java.time.Instant;
import java.util.Random;

@Component
@Slf4j
public class EventDataProducer {

    @GrpcClient("collector")
    private CollectorControllerGrpc.CollectorControllerBlockingStub collectorStub;


    private Sensors sensors;

    public void  start (){
        log.info("запуск сообщений с рандомными данными");
        for (int i =0; i<1000; i++){
            sensors.getClimateSensors().forEach(climateSensor -> sendEvent(createClimateSensorEvent(climateSensor)));

            sensors.getMotionSensors().forEach(motionSensor -> sendEvent(createMotionSensorEvent(motionSensor)));

            sensors.getLightSensors().forEach(lightSensor -> sendEvent(createLightSensorEvent(lightSensor)));

            sensors.getTemperatureSensors().forEach(temperatureSensor -> sendEvent(createTemperatureSensorEvent(temperatureSensor)));

            sensors.getClimateSensors().forEach(climateSensor -> sendEvent(createClimateSensorEvent(climateSensor)));

        }
        log.info("остановка сообщений с рандомными данными");
    }



    private SensorEventProto createTemperatureSensorEvent(TemperatureSensor sensor) {
        int temperatureCelsius = getRandomSensorValue(sensor.getTemperature());
        int temperatureFahrenheit = (int) (temperatureCelsius * 1.8 + 32);
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setTemperatureSensorEvent(
                        TemperatureSensorProto.newBuilder()
                                .setTemperatureC(temperatureCelsius)
                                .setTemperatureF(temperatureFahrenheit)
                                .build()
                )
                .build();
    }

private SensorEventProto createSwitchSensorEvent (SwitchSensor sensor){
    Instant ts = Instant.now();

    return SensorEventProto.newBuilder()
            .setId(sensor.getId())
            .setTimestamp(Timestamp.newBuilder().setSeconds(ts.getEpochSecond()).setNanos(ts.getNano()))
            .setSwitchSensorEvent(
                    SwitchSensorProto.newBuilder()
                            .setState(getRandomSwitchValue())
                            .build()
            )
            .build();

}


    private SensorEventProto createMotionSensorEvent(MotionSensor sensor) {
        int linkQuality = getRandomSensorValue(sensor.getLinkQuality());
        int voltage = getRandomSensorValue(sensor.getVoltage());
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setMotionSensorEvent(
                        MotionSensorProto.newBuilder()
                                .setLinkQuality(linkQuality)
                                .setMotion(getRandomSwitchValue())
                                .setVoltage(voltage)
                                .build()
                )
                .build();
    }


    private SensorEventProto createLightSensorEvent(LightSensor sensor) {
        int linkQuality = getRandomSensorValue(sensor.getLinkQuality());
        int luminosity = getRandomSensorValue(sensor.getLuminosity());
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setLightSensorEvent(
                        LightSensorProto.newBuilder()
                                .setLinkQuality(linkQuality)
                                .setLuminosity(luminosity)
                                .build()
                )
                .build();
    }


    private SensorEventProto createClimateSensorEvent(ClimateSensor sensor) {
        int temperature = getRandomSensorValue(sensor.getTemperatureC());
        int humidity = getRandomSensorValue(sensor.getHumidity());
        int co2Level = getRandomSensorValue(sensor.getCo2Level());
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setClimateSensorEvent(
                        ClimateSensorProto.newBuilder()
                                .setTemperatureC(temperature)
                                .setHumidity(humidity)
                                .setCo2Level(co2Level)
                                .build()
                )
                .build();
    }


    private void sendEvent(SensorEventProto event) {
        log.info("Отправляю данные: {}", event.getAllFields());
        CollectorResponse response = collectorStub.collectSensorEvent(event);
        log.info("Получил ответ от коллектора: {}", response);
    }

    private int getRandomSensorValue (MinMaxValue prevValue){
        return  (int)(((prevValue.getMaxValue()- prevValue.getMinValue())+1)*Math.random()+ prevValue.getMinValue());
}

    private  boolean getRandomSwitchValue (){
        Random random = new Random();
        return random.nextBoolean();
    }

}
