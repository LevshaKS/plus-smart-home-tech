package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.Setter;
import org.checkerframework.checker.units.qual.N;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "sensor")
public class Sensors {

    List<LightSensor> lightSensors;

    List<ClimateSensor> climateSensors;

    List <MotionSensor> motionSensors;

    List <SwitchSensor> switchSensors;

    List <TemperatureSensor> temperatureSensors;

}

