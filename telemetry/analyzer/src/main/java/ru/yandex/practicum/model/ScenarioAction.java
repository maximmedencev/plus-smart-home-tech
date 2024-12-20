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
@Table(name = "scenario_actions")
@IdClass(ScenarioActionsPk.class)
@ToString
public class ScenarioAction {
    @Id
    private long scenarioId;
    @Id
    private String sensorId;
    @Id
    private long actionId;
}
