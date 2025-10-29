package ru.yandex.practicum.analyzer.repistory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.Actions;
import ru.yandex.practicum.analyzer.model.Scenarios;


import java.util.List;


public interface ActionsRepository extends JpaRepository<Actions, Long> {

    void deleteByScenario(Scenarios scenario);

    List<Actions> findAllByScenario(Scenarios scenario);
}
