package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClimateSensor {

    private String id;

    private MinMaxValue temperatureC;
    private MinMaxValue humidity;
    private MinMaxValue co2Level;

}
