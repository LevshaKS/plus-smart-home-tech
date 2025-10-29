package ru.yandex.practicum.analyzer.repistory;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.Scenarios;


import java.util.List;
import java.util.Optional;

public interface ScenariosRepository extends JpaRepository<Scenarios, Long> {

    List<Scenarios> findByHubId(String hubId);


    Optional<Scenarios> findByHubIdAndName(String hubId, String name);
}
