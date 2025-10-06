package ru.pacticum.model.sensorEvent;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

//Событие датчика освещённости.
@Getter
@ToString
public class LightSensorEvent extends SensorEvent {

    @NotNull
    private int linkQuality;
    @NotNull
    private int luminosity;

    @Override
    public SensorEventType getType() {
        return SensorEventType.LIGHT_SENSOR_EVENT;
    }
}
