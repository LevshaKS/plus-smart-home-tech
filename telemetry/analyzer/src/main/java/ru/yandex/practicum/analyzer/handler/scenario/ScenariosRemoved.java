package ru.yandex.practicum.analyzer.handler.scenario;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.HubHandler;
import ru.yandex.practicum.analyzer.model.Scenarios;
import ru.yandex.practicum.analyzer.repistory.ActionsRepository;
import ru.yandex.practicum.analyzer.repistory.ConditionsRepository;
import ru.yandex.practicum.analyzer.repistory.ScenariosRepository;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioRemovedEventAvro;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScenariosRemoved implements HubHandler {
    private final ScenariosRepository scenariosRepository;
    private final ConditionsRepository conditionsRepository;
    private final ActionsRepository actionsRepository;

    @Override
    public String getMessageType() {
        return ScenarioRemovedEventAvro.class.getSimpleName();
    }
    @Transactional
    @Override
    public void handler(HubEventAvro hubEvent) {
        ScenarioRemovedEventAvro event = (ScenarioRemovedEventAvro) hubEvent.getPayload();
        Optional<Scenarios> scenario = scenariosRepository.findByHubIdAndName(hubEvent.getHubId(), event.getName());
        if (scenario.isPresent()) {
            actionsRepository.deleteByScenario(scenario.get());
            conditionsRepository.deleteByScenario(scenario.get());
         scenariosRepository.delete(scenario.get());
         log.info("сценарий удален");
        } else {
        log.warn("сценарий не найден");
        }
    }
}
