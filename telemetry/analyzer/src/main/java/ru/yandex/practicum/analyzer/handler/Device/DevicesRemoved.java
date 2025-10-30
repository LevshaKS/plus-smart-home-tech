package ru.yandex.practicum.analyzer.handler.Device;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.HubHandler;
import ru.yandex.practicum.analyzer.repistory.SensorsRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceRemovedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

@Component
@RequiredArgsConstructor
@Slf4j
public class DevicesRemoved implements HubHandler {

    private final SensorsRepository sensorsRepository;

    @Override
    public String getMessageType() {
        return DeviceRemovedEventAvro.class.getSimpleName();
    }

    @Override
    public void handler(HubEventAvro hubEvent) {
        DeviceRemovedEventAvro event = (DeviceRemovedEventAvro) hubEvent.getPayload();
        log.info("удалили сенсор в базу из звпроса" + hubEvent);
        sensorsRepository.deleteByIdAndHubId(event.getId(), hubEvent.getHubId());
    }
}
