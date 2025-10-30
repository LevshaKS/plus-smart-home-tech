package ru.yandex.practicum.analyzer.handler.scenario;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.handler.HubHandler;
import ru.yandex.practicum.analyzer.model.Actions;
import ru.yandex.practicum.analyzer.model.Conditions;
import ru.yandex.practicum.analyzer.model.Scenarios;
import ru.yandex.practicum.analyzer.repistory.ActionsRepository;
import ru.yandex.practicum.analyzer.repistory.ConditionsRepository;
import ru.yandex.practicum.analyzer.repistory.ScenariosRepository;
import ru.yandex.practicum.analyzer.repistory.SensorsRepository;
import ru.yandex.practicum.kafka.telemetry.event.DeviceActionAvro;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioAddedEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.ScenarioConditionAvro;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Slf4j
public class ScenariosAdded implements HubHandler {
    private final ScenariosRepository scenariosRepository;
    private final ConditionsRepository conditionsRepository;
    private final ActionsRepository actionsRepository;
    private final SensorsRepository sensorsRepository;

    @Override
    public String getMessageType() {
        return ScenarioAddedEventAvro.class.getSimpleName();
    }

    @Transactional
    @Override
    public void handler(HubEventAvro hubEvent) {
        ScenarioAddedEventAvro event = (ScenarioAddedEventAvro) hubEvent.getPayload();
        Optional<Scenarios> scenarios = (scenariosRepository.findByHubIdAndName(hubEvent.getHubId(), event.getName()));
        if (scenarios.isEmpty()) {
            log.info("добавляем запись в таблицу scenario");
             scenarios = Optional.of(new Scenarios(null, hubEvent.getHubId(), event.getName()));
            scenariosRepository.save(scenarios.get());
        }

        if (sensorsRepository.existsByIdInAndHubId(event.getConditions().
                stream()
                .map(ScenarioConditionAvro::getSensorId)
                .toList(), hubEvent.getHubId())) {

            Optional<Scenarios> finalScenarios = scenarios;
            Set<Conditions> conditionsSet = event.getConditions().stream()
                    .map(c -> (new Conditions(null,
                            c.getType(),
                            c.getOperation(),
                            convertValue(c.getValue()),
                            finalScenarios.get(),
                            sensorsRepository.findById((c.getSensorId())).orElseThrow())))
                    .collect(Collectors.toSet());
            log.info("добавляем запись в таблицу condition");
            conditionsRepository.saveAll(conditionsSet);
        }

        if (sensorsRepository.existsByIdInAndHubId(event.getActions()
                .stream()
                .map(DeviceActionAvro::getSensorId)
                .toList(), hubEvent.getHubId())) {

            Optional<Scenarios> finalScenarios = scenarios;
            Set<Actions> actionsSet = event.getActions().stream()
                    .map(d -> new Actions(null,
                            d.getType(),
                            convertValue(d.getValue()),
                            finalScenarios.get(),
                            sensorsRepository.findById(d.getSensorId()).orElseThrow()))
                    .collect(Collectors.toSet());
            log.info("добавляем запись в таблицу action");
            actionsRepository.saveAll(actionsSet);
        }




    }
    private Integer convertValue(Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            return (Boolean) value ? 1 : 0;
        }
    }
}

