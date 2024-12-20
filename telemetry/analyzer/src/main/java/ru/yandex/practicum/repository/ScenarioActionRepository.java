package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.ScenarioAction;
import ru.yandex.practicum.model.ScenarioActionsPk;

import java.util.List;

@Repository
public interface ScenarioActionRepository extends JpaRepository<ScenarioAction, ScenarioActionsPk> {
    void deleteByScenarioId(long scenarioId);

    void deleteBySensorId(String sensorId);

    List<ScenarioAction> findByScenarioIdAndSensorId(Long scenarioId, String sensorId);

    @Query("SELECT actionId FROM ScenarioAction sa WHERE sa.scenarioId = :scenarioId")
    List<Long> findActionIdsByScenarioId(@Param("scenarioId") Long scenarioId);

    @Query("SELECT actionId FROM ScenarioAction sa WHERE sa.sensorId = :sensorId")
    List<Long> findActionIdsBySensorId(@Param("sensorId") String sensorId);

    List<ScenarioAction> findByScenarioId(Long scenarioId);

}
