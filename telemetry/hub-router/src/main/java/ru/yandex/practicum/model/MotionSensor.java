package ru.yandex.practicum.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MotionSensor {
    private String id;
    private MinMaxValue linkQuality;
    private MinMaxValue voltage;
}
