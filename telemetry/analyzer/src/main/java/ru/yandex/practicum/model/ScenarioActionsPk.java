package ru.yandex.practicum.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class ScenarioActionsPk {
    @Column(name = "scenario_id")
    long scenarioId;
    @Column(name = "sensor_id")
    String sensorId;
    @Column(name = "action_id")
    long actionId;
}
