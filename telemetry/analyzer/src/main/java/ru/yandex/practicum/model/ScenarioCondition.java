package ru.yandex.practicum.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "scenario_conditions")
@ToString
@IdClass(ScenarioConditionsPk.class)
public class ScenarioCondition {
    @Id
    private long scenarioId;
    @Id
    private String sensorId;
    @Id
    private long conditionId;
}