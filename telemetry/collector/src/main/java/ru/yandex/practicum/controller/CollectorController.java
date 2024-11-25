package ru.yandex.practicum.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.CollectorTopics;
import ru.yandex.practicum.event.hub.HubEvent;
import ru.yandex.practicum.event.sensor.SensorEvent;
import ru.yandex.practicum.service.CollectorService;


@RestController
@RequestMapping("/events/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CollectorController {

    private final CollectorService collectorService;

    @PostMapping("/sensors")
    public SensorEvent collectSensorEvent(@Valid @RequestBody SensorEvent event) {
        return collectorService.collectSensorEvent(event, CollectorTopics.TELEMETRY_SENSORS_TOPIC);
    }

    @PostMapping("/hubs")
    public HubEvent collectHubEvent(@Valid @RequestBody HubEvent event) {
        return collectorService.collectHubEvent(event, CollectorTopics.TELEMETRY_HUBS_TOPIC);
    }
}

