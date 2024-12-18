package ru.yandex.practicum.processor;

import com.google.protobuf.Timestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.ActionTypeProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionProto;
import ru.yandex.practicum.grpc.telemetry.event.DeviceActionRequest;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.*;
import ru.yandex.practicum.service.GrpcClientService;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

@Slf4j
@Component
@RequiredArgsConstructor
public class SnapshotProcessor {

    @Value("${topics.snapshots}")
    private String snapshotsTopic;

    @Value("${bootstrap_server}")
    private String bootstrapServer;

    @Value("${key_deserializer_class}")
    private String keyDeserializerClass;

    @Value("${snapshots_value_deserializer_class}")
    private String snapshotsValueDeserializerClass;

    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ConditionRepository conditionRepository;

    private final GrpcClientService grpcClientService;

    public void start() {
        Properties snapshotsConsumerConfig = new Properties();

        snapshotsConsumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "groupAnalyzerSnapshotsConsumer");
        snapshotsConsumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        snapshotsConsumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        snapshotsConsumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass);
        snapshotsConsumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, snapshotsValueDeserializerClass);

        Consumer<String, SensorsSnapshotAvro> snapshotsConsumer = new KafkaConsumer<>(snapshotsConsumerConfig);

        try {
            snapshotsConsumer.subscribe(List.of(snapshotsTopic));
            while (true) {
                ConsumerRecords<String, SensorsSnapshotAvro> snapshotsRecords =
                        snapshotsConsumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, SensorsSnapshotAvro> record : snapshotsRecords) {
                    log.info("\nПолучено сообщение из партиции {}, со смещением {}:\n{}",
                            record.partition(), record.offset(), record.value());
                    SensorsSnapshotAvro snapshot = record.value();
                    List<Scenario> hubScenarios = scenarioRepository.findByHubId(snapshot.getHubId());

                    hubScenarios.forEach(scenario -> processScenario(snapshot, scenario));
                }
            }


        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки снапшота", e);
        } finally {

            try {
                snapshotsConsumer.commitSync();

            } finally {
                log.info("Закрываем консьюмер");
                snapshotsConsumer.close();
            }
        }
    }


    private void processScenario(SensorsSnapshotAvro snapshot, Scenario scenario) {

        List<ScenarioCondition> scenarioConditions = scenarioConditionRepository.findByScenarioId(scenario.getId());

        for (ScenarioCondition scenarioCondition : scenarioConditions) {
            Optional<Condition> optionalCondition = conditionRepository.findById(scenarioCondition.getConditionId());
            if (optionalCondition.isEmpty()) {
                log.error("Condition с id ={} не найдено в таблице conditions", scenarioCondition.getConditionId());
                return;
            }
            Condition condition = optionalCondition.get();
            SensorStateAvro sensorStateAvro = snapshot.getSensorsState().get(scenarioCondition.getSensorId());


            if (sensorStateAvro != null) {
                boolean actionApproved = approveAction(sensorStateAvro,
                        condition.getType(),
                        condition.getValue(),
                        condition.getOperation());

                if (actionApproved) {
                    List<ScenarioAction> scenarioActions = scenarioActionRepository
                            .findByScenarioId(scenario.getId());

                    for (ScenarioAction scenarioAction : scenarioActions) {
                        Action action = actionRepository.findById(scenarioAction.getActionId()).get();

                        Instant ts = Instant.now();

                        grpcClientService.sendData(DeviceActionRequest.newBuilder()
                                .setHubId(scenario.getHubId())
                                .setScenarioName(scenario.getName())
                                .setTimestamp(Timestamp.newBuilder()
                                        .setSeconds(ts.getEpochSecond())
                                        .setNanos(ts.getNano())
                                        .build())
                                .setAction(DeviceActionProto.newBuilder()
                                        .setType(ActionTypeProto.valueOf(action.getType().toString()))
                                        .setSensorId(scenarioAction.getSensorId())
                                        .setValue(action.getValue())
                                        .build())
                                .build());
                    }
                }
            }
        }
    }

    private class IntegerStateToCompare {
        long a;
        long b;
        ConditionOperation conditionOperation;

        IntegerStateToCompare(long a, long b, ConditionOperation conditionOperation) {
            this.a = a;
            this.b = b;
            this.conditionOperation = conditionOperation;
        }
    }

    private class BooleanStateToCompare {
        Boolean a;
        Integer b;
        ConditionOperation conditionOperation;

        BooleanStateToCompare(Boolean a, Integer b, ConditionOperation conditionOperation) {
            this.a = a;
            this.b = b;
            this.conditionOperation = conditionOperation;
        }
    }

    private boolean approveAction(SensorStateAvro sensorStateAvro, ConditionType conditionType, Integer value,
                                  ConditionOperation conditionOperation) {
        Predicate<IntegerStateToCompare> isGreaterInteger = data -> (data.a > data.b)
                && data.conditionOperation == ConditionOperation.GREATER_THAN;

        Predicate<IntegerStateToCompare> isLowerInteger = data -> (data.a < data.b)
                && data.conditionOperation == ConditionOperation.LOWER_THAN;

        Predicate<IntegerStateToCompare> isEqualsInteger = data -> (data.a == data.b)
                && data.conditionOperation == ConditionOperation.EQUALS;

        Predicate<BooleanStateToCompare> isGreaterBoolean = data -> ((data.a ? 1 : 0) > data.b)
                && data.conditionOperation == ConditionOperation.GREATER_THAN;

        Predicate<BooleanStateToCompare> isLowerBoolean = data -> ((data.a ? 1 : 0) < data.b)
                && data.conditionOperation == ConditionOperation.LOWER_THAN;

        Predicate<BooleanStateToCompare> isEqualsBoolean = data -> ((data.a ? 1 : 0) == data.b)
                && data.conditionOperation == ConditionOperation.EQUALS;

        Object sensorStateAvroValue = switch (conditionType) {
            case CO2LEVEL -> ((ClimateSensorAvro) sensorStateAvro.getData())
                    .getCo2Level();
            case HUMIDITY -> ((ClimateSensorAvro) sensorStateAvro.getData())
                    .getHumidity();
            case MOTION -> ((MotionSensorAvro) sensorStateAvro.getData())
                    .getMotion();
            case LUMINOSITY -> ((LightSensorAvro) sensorStateAvro.getData())
                    .getLuminosity();
            case SWITCH -> ((SwitchSensorAvro) sensorStateAvro.getData())
                    .getState();
            case TEMPERATURE -> ((TemperatureSensorAvro) sensorStateAvro.getData())
                    .getTemperatureC();
        };


        switch (sensorStateAvroValue) {
            case Integer a:
                if (isEqualsInteger.test(new IntegerStateToCompare(a, value, conditionOperation)) ||
                        isGreaterInteger.test(new IntegerStateToCompare(a, value, conditionOperation)) ||
                        isLowerInteger.test(new IntegerStateToCompare(a, value, conditionOperation))) {
                    return true;
                }
                break;
            case Boolean a:
                if (isEqualsBoolean.test(new BooleanStateToCompare(a, value, conditionOperation)) ||
                        isGreaterBoolean.test(new BooleanStateToCompare(a, value, conditionOperation)) ||
                        isLowerBoolean.test(new BooleanStateToCompare(a, value, conditionOperation))) {
                    return true;
                }
                break;
            default:
                throw new IllegalStateException("Неизвестный тип данных: " + sensorStateAvro.getData());
        }
        return false;
    }
}
