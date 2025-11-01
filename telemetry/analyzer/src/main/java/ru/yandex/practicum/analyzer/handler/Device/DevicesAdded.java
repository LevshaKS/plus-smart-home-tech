package ru.yandex.practicum.analyzer.handler.Device;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.HubHandler;
import ru.yandex.practicum.analyzer.model.Sensors;
import ru.yandex.practicum.analyzer.repistory.SensorsRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class DevicesAdded implements HubHandler {
    private final SensorsRepository sensorsRepository;

    @Override
    public String getMessageType() {
        return DeviceAddedEventAvro.class.getSimpleName();
    }

    @Override
    @Transactional
    public void handler(HubEventAvro hubEvent) {
        DeviceAddedEventAvro event = (DeviceAddedEventAvro) hubEvent.getPayload();
        log.info("сохранили сенсор в базу из звпроса" + hubEvent);
        sensorsRepository.save(new Sensors(event.getId(), hubEvent.getHubId()));
    }


}
