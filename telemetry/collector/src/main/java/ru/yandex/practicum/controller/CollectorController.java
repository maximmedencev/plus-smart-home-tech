package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.event.hub.HubEvent;
import ru.yandex.practicum.event.sensor.SensorEvent;
import ru.yandex.practicum.service.CollectorService;


@RestController
@RequestMapping("/events/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CollectorController {

    @Value("${topics.sensors}")
    private String sensorsTopic;

    @Value("${topics.hubs}")
    private String hubsTopic;


    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public SensorEvent collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        return collectorService
                .collectSensorEvent(event, sensorsTopic);
    }

    @PostMapping("/hubs")
    public HubEvent collectHubEvent(@Valid @RequestBody HubEvent event) {
        return collectorService
                .collectHubEvent(event, hubsTopic);
    }
}

