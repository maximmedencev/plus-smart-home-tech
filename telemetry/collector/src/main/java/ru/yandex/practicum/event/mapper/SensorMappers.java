package ru.yandex.practicum.event.mapper;

import ru.yandex.practicum.event.sensor.*;
import ru.yandex.practicum.grpc.telemetry.event.SensorEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.grpc.telemetry.event.ClimateSensorEventProto;

public class SensorMappers {
    public static ClimateSensorAvro maptoToClimateSensorAvro(ClimateSensorEvent event) {
        return ClimateSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setHumidity(event.getHumidity())
                .setCo2Level(event.getCo2Level())
                .setTemperatureC(event.getTemperatureC())
                .build();
    }

    public static LightSensorAvro maptoToLightSensorAvro(LightSensorEvent event) {
        return LightSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setLinkQuality(event.getLinkQuality())
                .setLuminosity(event.getLuminosity())
                .build();
    }

    public static MotionSensorAvro maptoToMotionSensorAvro(MotionSensorEvent event) {
        return MotionSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setMotion(event.isMotion())
                .setLinkQuality(event.getLinkQuality())
                .setVoltage(event.getVoltage())
                .build();
    }

    public static SwitchSensorAvro mapToSwitchSensorAvro(SwitchSensorEvent event) {
        return SwitchSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setState(event.isState())
                .build();
    }

    public static TemperatureSensorAvro mapToTemperatureSensorAvro(TemperatureSensorEvent event) {
        return TemperatureSensorAvro.newBuilder()
                .setId(event.getId())
                .setHubId(event.getHubId())
                .setTimestamp(event.getTimestamp())
                .setTemperatureC(event.getTemperatureC())
                .setTemperatureF(event.getTemperatureF())
                .build();
    }

}
