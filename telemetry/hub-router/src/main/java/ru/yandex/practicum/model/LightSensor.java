package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LightSensor {
    private String id;
    private MinMaxValue linkQuality;
    private MinMaxValue luminosity;

}
