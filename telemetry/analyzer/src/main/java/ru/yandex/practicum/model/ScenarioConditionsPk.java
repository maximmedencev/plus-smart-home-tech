package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ScenarioConditionsPk {
    @Column(name = "scenario_id")
    long scenarioId;
    @Column(name = "sensor_id")
    String sensorId;
    @Column(name = "condition_id")
    long conditionId;
}
