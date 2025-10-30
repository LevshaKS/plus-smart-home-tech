package ru.yandex.practicum.analyzer.repistory;


import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.analyzer.model.Sensors;

import java.util.Collection;
import java.util.Optional;

public interface SensorsRepository extends JpaRepository<Sensors, String> {

    boolean existsByIdInAndHubId(Collection<String> id, String hubId);

    Optional<Sensors> findByIdAndHubId(String id, String hubId);


    void deleteByIdAndHubId(String id, String hubId);
}
