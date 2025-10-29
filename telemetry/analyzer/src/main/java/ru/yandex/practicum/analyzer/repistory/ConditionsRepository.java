package ru.yandex.practicum.analyzer.repistory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.Conditions;
import ru.yandex.practicum.analyzer.model.Scenarios;


import java.util.List;

public interface ConditionsRepository extends JpaRepository<Conditions, Long> {

    void deleteByScenario(Scenarios scenario);

    List<Conditions> findAllByScenario(Scenarios scenario);
}
