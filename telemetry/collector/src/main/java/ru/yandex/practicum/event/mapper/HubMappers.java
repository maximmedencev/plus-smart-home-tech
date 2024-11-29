package ru.yandex.practicum.event.mapper;

import ru.yandex.practicum.event.hub.*;
import ru.yandex.practicum.kafka.telemetry.event.*;

public class HubMappers {
    public static HubEventAvro mapToToHubEventAvro(DeviceAddedEvent event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(DeviceAddedEventAvro.newBuilder()
                        .setId(event.getId())
                        .setType(DeviceTypeAvro.valueOf(event.getDeviceType().toString()))
                        .build())
                .build();
    }

    public static HubEventAvro mapToToHubEventAvro(DeviceRemovedEvent event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(DeviceRemovedEventAvro.newBuilder()
                        .setId(event.getId())
                        .build())
                .build();
    }

    public static HubEventAvro mapToHubEventAvro(ScenarioAddedEvent event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(ScenarioAddedEventAvro.newBuilder()
                        .setConditions(event.getConditions().stream()
                                .map(HubMappers::mapToScenarioConditionAvro)
                                .toList())
                        .setActions(event.getActions().stream()
                                .map(HubMappers::mapToDeviceActionAvro)
                                .toList())
                        .build())
                .build();
    }

    public static HubEventAvro mapToHubEventAvro(ScenarioRemovedEvent event) {
        return HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setPayload(ScenarioRemovedEventAvro.newBuilder()
                        .setName(event.getName()))
                .build();
    }


    private static ScenarioConditionAvro mapToScenarioConditionAvro(ScenarioCondition condition) {
        return ScenarioConditionAvro.newBuilder()
                .setSensorId(condition.getSensorId())
                .setType(ActionTypeAvro.valueOf(condition.getType().toString()))
                .setOperation(ConditionOperationAvro.valueOf(condition.getOperation().toString()))
                .setValue(condition.getValue())
                .build();
    }

    private static DeviceActionAvro mapToDeviceActionAvro(DeviceAction action) {
        return DeviceActionAvro.newBuilder()
                .setSensorId(action.getSensorId())
                .setSensorId(action.getSensorId())
                .setValue(action.getValue())
                .build();
    }

}
