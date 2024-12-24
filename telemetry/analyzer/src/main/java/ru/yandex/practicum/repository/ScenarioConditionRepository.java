package ru.yandex.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.model.ScenarioCondition;
import ru.yandex.practicum.model.ScenarioConditionsPk;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScenarioConditionRepository extends JpaRepository<ScenarioCondition, ScenarioConditionsPk> {
    void deleteByScenarioId(long scenarioId);

    void deleteBySensorId(String sensorId);

    Optional<ScenarioCondition> findByScenarioIdAndSensorId(Long scenarioId, String sensorId);

    @Query("SELECT conditionId FROM ScenarioCondition sc WHERE sc.scenarioId = :sid")
    List<Long> findConditionIdsByScenarioId(@Param("sid") Long scenarioId);

    @Query("SELECT conditionId FROM ScenarioCondition sa WHERE sa.sensorId = :sensorId")
    List<Long> findConditionIdsBySensorId(@Param("sensorId") String sensorId);

    List<ScenarioCondition> findByScenarioId(Long scenarioId);

}
