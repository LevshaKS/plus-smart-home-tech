package ru.yandex.practicum.aggregator.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.stereotype.Service;
//import ru.yandex.practicum.aggregator.config.KafkaConfig;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j

public class SnapshotServiceImpl implements SnapshotService {
    private final Map<String, SensorsSnapshotAvro> snapshotsMap = new HashMap<>(); //мапа снимков состояния




    @Override
    public Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        String hubId = event.getHubId();

        if (!snapshotsMap.containsKey(hubId)) { //если снимка еще небыло то создаем новый снимок состояния датчиков
            SensorStateAvro sensorStateAvro = SensorStateAvro.newBuilder() //создаем список сенсоров
                    .setTimestamp(event.getTimestamp())
                    .setData(event.getPayload())
                    .build();

            Map<String, SensorStateAvro> sensorStateMap = new HashMap<>();
            //добавялем список сенсерво по id сенсора
            sensorStateMap.put(event.getId(), sensorStateAvro);

            //Создаем снимок
            SensorsSnapshotAvro snapshot = SensorsSnapshotAvro.newBuilder()
                    .setHubId(hubId)
                    .setTimestamp(Instant.now())
                    .setSensorsState(sensorStateMap)
                    .build();
            snapshotsMap.put(hubId, snapshot);  //добавляем снимок  в мапу снимков по хаб ид
//возращаем снимок
           log.info("создали новый снимок " + snapshot.toString());
            return Optional.of(snapshot);

        } else {
            SensorsSnapshotAvro oldSnapshot = snapshotsMap.get(hubId); //возвращаем спимок по ид хаба
            Map<String, SensorStateAvro> oldSensorStateMap = oldSnapshot.getSensorsState();

            //проверяем если если статус сенсора позже или не отличается от сесора в запросе то
            if (oldSensorStateMap.containsKey(event.getId())) {
                SensorStateAvro oldState = oldSensorStateMap.get(event.getId());
                if (oldState.getTimestamp().isAfter(event.getTimestamp()) || oldState.getData().equals(event.getPayload())) {
                   log.info("вернули пустой запрос, нечего обновлять");
                    return Optional.empty(); //возврващаем пустой запрос
                }
            } //записываем статусы в снимок
            SensorStateAvro sensorStateAvro = SensorStateAvro.newBuilder()
                    .setTimestamp(event.getTimestamp())
                    .setData(event.getPayload())
                    .build();
            oldSnapshot.getSensorsState().put(event.getId(), sensorStateAvro);
            oldSnapshot.setTimestamp(event.getTimestamp());
            log.info("вернули обновленный запрос "+ oldSnapshot);
            return Optional.of(oldSnapshot);
        }
    }

}