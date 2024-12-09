package ru.yandex.practicum;

import com.google.protobuf.Timestamp;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.collector.CollectorControllerGrpc;
import ru.yandex.practicum.grpc.telemetry.event.*;
import ru.yandex.practicum.sensors.*;

import java.time.Instant;
import java.util.Random;

@Component
public class EventDataProducer {
    private static final Logger log = LoggerFactory.getLogger(EventDataProducer.class);

    @GrpcClient("collector")
    private CollectorControllerGrpc.CollectorControllerBlockingStub collectorStub;

    Random random = new Random();
    private final static String HUB_ID = "hub-router";

    @Value("${sensor.motionSensors[0].id}")
    private String motionSensorId1;
    @Value("${sensor.motionSensors[0].linkQuality.minValue}")
    private int motionSensorLinkQualityMin1;
    @Value("${sensor.motionSensors[0].linkQuality.maxValue}")
    private int motionSensorLinkQualityMax1;
    @Value("${sensor.motionSensors[0].voltage.minValue}")
    private int motionSensorVoltageMin1;
    @Value("${sensor.motionSensors[0].voltage.maxValue}")
    private int motionSensorVoltageMax1;

    @Value("${sensor.motionSensors[1].id}")
    private String motionSensorId2;
    @Value("${sensor.motionSensors[1].linkQuality.minValue}")
    private int motionSensorLinkQualityMin2;
    @Value("${sensor.motionSensors[1].linkQuality.maxValue}")
    private int motionSensorLinkQualityMax2;
    @Value("${sensor.motionSensors[1].voltage.minValue}")
    private int motionSensorVoltageMin2;
    @Value("${sensor.motionSensors[1].voltage.maxValue}")
    private int motionSensorVoltageMax2;

    @Value("${sensor.switchSensors[0].id}")
    private String switchSensorId1;

    @Value("${sensor.temperatureSensors[0].id}")
    private String temperatureSensorId1;
    @Value("${sensor.temperatureSensors[0].temperature.minValue}")
    private int temperatureSensorMin1;
    @Value("${sensor.temperatureSensors[0].temperature.maxValue}")
    private int temperatureSensorMax1;

    @Value("${sensor.temperatureSensors[1].id}")
    private String temperatureSensorId2;
    @Value("${sensor.temperatureSensors[1].temperature.minValue}")
    private int temperatureSensorMin2;
    @Value("${sensor.temperatureSensors[1].temperature.maxValue}")
    private int temperatureSensorMax2;

    @Value("${sensor.lightSensors[0].id}")
    private String lightSensorId1;
    @Value("${sensor.lightSensors[0].luminosity.minValue}")
    private int lightSensorMin1;
    @Value("${sensor.lightSensors[0].luminosity.maxValue}")
    private int lightSensorMax1;

    @Value("${sensor.lightSensors[1].id}")
    private String lightSensorId2;
    @Value("${sensor.lightSensors[1].luminosity.minValue}")
    private int lightSensorMin2;
    @Value("${sensor.lightSensors[1].luminosity.maxValue}")
    private int lightSensorMax2;

    @Value("${sensor.climateSensors[0].id}")
    private String climateSensorId1;
    @Value("${sensor.climateSensors[0].temperature.minValue}")
    private int climateSensorTemperatureMin1;
    @Value("${sensor.climateSensors[0].temperature.maxValue}")
    private int climateSensorTemperatureMax1;
    @Value("${sensor.climateSensors[0].humidity.minValue}")
    private int climateSensorHumidityMin1;
    @Value("${sensor.climateSensors[0].humidity.maxValue}")
    private int climateSensorHumidityMax1;
    @Value("${sensor.climateSensors[0].co2Level.minValue}")
    private int climateSensorCo2LevelMin1;
    @Value("${sensor.climateSensors[0].co2Level.maxValue}")
    private int climateSensorCo2LevelMax1;

    @Value("${sensor.climateSensors[1].id}")
    private String climateSensorId2;
    @Value("${sensor.climateSensors[1].temperature.minValue}")
    private int climateSensorTemperatureMin2;
    @Value("${sensor.climateSensors[1].temperature.maxValue}")
    private int climateSensorTemperatureMax2;
    @Value("${sensor.climateSensors[1].humidity.minValue}")
    private int climateSensorHumidityMin2;
    @Value("${sensor.climateSensors[1].humidity.maxValue}")
    private int climateSensorHumidityMax2;
    @Value("${sensor.climateSensors[1].co2Level.minValue}")
    private int climateSensorCo2LevelMin2;
    @Value("${sensor.climateSensors[1].co2Level.maxValue}")
    private int climateSensorCo2LevelMax2;

    @Scheduled(initialDelay = 1000, fixedDelay = 1000)
    public void sendData() {
        int randomNumber = (int) (Math.random() * 9) + 1;

        switch (randomNumber) {
            case 1:
                sendEvent(createMotionSensorEvent(
                        new MotionSensor(
                                motionSensorId1,
                                getRandomSensorValueInRange(
                                        motionSensorLinkQualityMin1,
                                        motionSensorLinkQualityMax1),
                                getRandomSensorValueInRange(
                                        motionSensorVoltageMin1,
                                        motionSensorVoltageMax1),
                                random.nextBoolean()
                        )));
                break;
            case 2:
                sendEvent(createMotionSensorEvent(
                        new MotionSensor(
                                motionSensorId1,
                                getRandomSensorValueInRange(
                                        motionSensorLinkQualityMin2,
                                        motionSensorLinkQualityMax2),
                                getRandomSensorValueInRange(
                                        motionSensorVoltageMin2,
                                        motionSensorVoltageMax2),
                                random.nextBoolean()
                        )));
                break;
            case 3:
                sendEvent(createSwitchSensorEvent(
                        new SwitchSensor(
                                switchSensorId1,
                                random.nextBoolean()
                        )));
                break;
            case 4:
                sendEvent(createTemperatureSensorEvent(
                        new TemperatureSensor(
                                temperatureSensorId1,
                                getRandomSensorValueInRange(
                                        temperatureSensorMin1,
                                        temperatureSensorMax1))));
                break;
            case 5:
                sendEvent(createTemperatureSensorEvent(
                        new TemperatureSensor(
                                temperatureSensorId2,
                                getRandomSensorValueInRange(
                                        temperatureSensorMin2,
                                        temperatureSensorMax2))));
                break;
            case 6:
                sendEvent(createLightSensorEvent(
                        new LightSensor(
                                lightSensorId1,
                                getRandomSensorValueInRange(
                                        lightSensorMin1,
                                        lightSensorMax1),
                                (int) (Math.random() * 100 + 1))));
                break;
            case 7:
                sendEvent(createLightSensorEvent(
                        new LightSensor(
                                lightSensorId2,
                                getRandomSensorValueInRange(
                                        lightSensorMin2,
                                        lightSensorMax2),
                                (int) (Math.random() * 100 + 1))));
                break;
            case 8:
                sendEvent(createClimateSensorEvent(
                        new ClimateSensor(
                                climateSensorId1,
                                getRandomSensorValueInRange(
                                        climateSensorTemperatureMin1,
                                        climateSensorTemperatureMax1),
                                getRandomSensorValueInRange(
                                        climateSensorHumidityMin1,
                                        climateSensorHumidityMax1),
                                getRandomSensorValueInRange(
                                        climateSensorCo2LevelMin1,
                                        climateSensorCo2LevelMax1)
                        )));
                break;
            case 9:
                sendEvent(createClimateSensorEvent(
                        new ClimateSensor(
                                climateSensorId2,
                                getRandomSensorValueInRange(
                                        climateSensorTemperatureMin2,
                                        climateSensorTemperatureMax2),
                                getRandomSensorValueInRange(
                                        climateSensorHumidityMin2,
                                        climateSensorHumidityMax2),
                                getRandomSensorValueInRange(
                                        climateSensorCo2LevelMin2,
                                        climateSensorCo2LevelMax2)
                        )));
                break;
        }
    }

    private void sendEvent(SensorEventProto event) {
        log.info("Отправляю данные: {}", event.getAllFields());
        //CollectorResponse response = collectorStub.collectSensorEvent(event);
        collectorStub.collectSensorEvent(event);
        //log.info("Получил ответ от коллектора: {}", response);
    }

    private SensorEventProto createTemperatureSensorEvent(TemperatureSensor sensor) {
        int temperatureCelsius = sensor.getTemperature();
        int temperatureFahrenheit = (int) (temperatureCelsius * 1.8 + 32);
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setHubId(HUB_ID)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setTemperatureSensorEvent(
                        TemperatureSensorEventProto.newBuilder()
                                .setTemperatureC(temperatureCelsius)
                                .setTemperatureF(temperatureFahrenheit)
                                .build()
                )
                .build();
    }

    private SensorEventProto createMotionSensorEvent(MotionSensor sensor) {
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setHubId(HUB_ID)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setMotionSensorEvent(
                        MotionSensorEventProto.newBuilder()
                                .setLinkQuality(sensor.getLinkQuality())
                                .setVoltage(sensor.getVoltage())
                                .setMotion(sensor.isMotion())
                                .build()
                )
                .build();
    }

    private SensorEventProto createSwitchSensorEvent(SwitchSensor sensor) {
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setHubId(HUB_ID)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setSwitchSensorEvent(
                        SwitchSensorEventProto.newBuilder()
                                .setState(sensor.isState())
                                .build()
                )
                .build();
    }


    private SensorEventProto createLightSensorEvent(LightSensor sensor) {
        Instant ts = Instant.now();
        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setHubId(HUB_ID)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setLightSensorEvent(
                        LightSensorEventProto.newBuilder()
                                .setLuminosity(sensor.getLuminosity())
                                .setLinkQuality(sensor.getLinkQuality())
                                .build()
                )
                .build();
    }

    private SensorEventProto createClimateSensorEvent(ClimateSensor sensor) {
        Instant ts = Instant.now();

        return SensorEventProto.newBuilder()
                .setId(sensor.getId())
                .setHubId(HUB_ID)
                .setTimestamp(Timestamp.newBuilder()
                        .setSeconds(ts.getEpochSecond())
                        .setNanos(ts.getNano())
                ).setClimateSensorEvent(
                        ClimateSensorEventProto.newBuilder()
                                .setTemperatureC(sensor.getTemperature())
                                .setHumidity(sensor.getHumidity())
                                .setCo2Level(sensor.getCo2Level())
                                .build()
                )
                .build();
    }


    private int getRandomSensorValue(int value) {
        if (Math.random() > 0.5) {
            return value + (int) (Math.random() * value * 0.5);
        } else {
            return value - (int) (Math.random() * value * 0.5);
        }
    }

    private int getRandomSensorValueInRange(int min, int max) {
        return min + (int) (Math.abs(max - min) * Math.random());
    }


}