package ru.yandex.practicum.aggregator.service;


import ru.yandex.practicum.kafka.telemetry.event.*;
import java.util.Optional;



public interface SnapshotService {

     Optional<SensorsSnapshotAvro> updateState(SensorEventAvro sensors) ;

}
