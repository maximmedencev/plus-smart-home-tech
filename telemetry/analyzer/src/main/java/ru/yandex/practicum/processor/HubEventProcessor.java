package ru.yandex.practicum.processor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.*;
import ru.yandex.practicum.model.*;
import ru.yandex.practicum.repository.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Slf4j
@Component
@RequiredArgsConstructor
public class HubEventProcessor implements Runnable {
    @Value("${topics.hubs}")
    private String hubsTopic;

    @Value("${bootstrap_server}")
    private String bootstrapServer;

    @Value("${key_deserializer_class}")
    private String keyDeserializerClass;

    @Value("${hubevents_value_deserializer_class}")
    private String hubEventsValueDeserializerClass;

    private final ScenarioRepository scenarioRepository;
    private final ActionRepository actionRepository;
    private final SensorRepository sensorRepository;
    private final ScenarioActionRepository scenarioActionRepository;
    private final ScenarioConditionRepository scenarioConditionRepository;
    private final ConditionRepository conditionRepository;


    @Override
    public void run() {
        Properties hubEventsConsumerConfig = new Properties();

        hubEventsConsumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, "groupAnalyzerHubEventsConsumer");
        hubEventsConsumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        hubEventsConsumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        hubEventsConsumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass);
        hubEventsConsumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, hubEventsValueDeserializerClass);

        Consumer<String, HubEventAvro> hubEventConsumer = new KafkaConsumer<>(hubEventsConsumerConfig);

        try {
            hubEventConsumer.subscribe(List.of(hubsTopic));
            while (true) {
                ConsumerRecords<String, HubEventAvro> hubEventsRecords =
                        hubEventConsumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord<String, HubEventAvro> record : hubEventsRecords) {
                    log.info("Получено сообщение из партиции {}, со смещением {}:\n{}\n",
                            record.partition(), record.offset(), record.value());

                    String hubId = record.value().getHubId();
                    Object payload = record.value().getPayload();

                    switch (payload) {
                        case ScenarioAddedEventAvro scenarioAddedEventAvro:
                            processScenarioAddedEvent(scenarioAddedEventAvro, hubId);
                            break;
                        case ScenarioRemovedEventAvro scenarioRemovedEventAvro:
                            processScenarioRemovedEvent(scenarioRemovedEventAvro, hubId);
                            break;
                        case DeviceAddedEventAvro deviceAddedEventAvro:
                            processDeviceAddedEvent(deviceAddedEventAvro, hubId);
                            break;
                        case DeviceRemovedEventAvro deviceRemovedEventAvro:
                            processDeviceRemovedEvent(deviceRemovedEventAvro, hubId);
                            break;
                        default:
                            throw new IllegalStateException("Неизвестный класс записи: " + payload);
                    }

                }
            }
        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки события хаба", e);
        } finally {

            try {
                hubEventConsumer.commitSync();

            } finally {
                log.info("Закрываем консьюмер");
                hubEventConsumer.close();
            }
        }
    }

    private void processDeviceRemovedEvent(DeviceRemovedEventAvro payload, String hubId) {
        Optional<Sensor> optionalSensor = sensorRepository.findByIdAndHubId(payload.getId(), hubId);

        if (optionalSensor.isEmpty()) {
            return;
        }

        String sensorId = optionalSensor.get().getId();

        List<Long> actionIds = scenarioActionRepository
                .findActionIdsBySensorId(sensorId);
        List<Long> conditionIds = scenarioConditionRepository
                .findConditionIdsBySensorId(sensorId);

        scenarioActionRepository.deleteBySensorId(sensorId);
        scenarioConditionRepository.deleteBySensorId(sensorId);

        actionRepository.deleteAllById(actionIds);
        conditionRepository.deleteAllById(conditionIds);

        sensorRepository.deleteById(sensorId);
    }

    private void processDeviceAddedEvent(DeviceAddedEventAvro payload, String hubId) {
        if (sensorRepository.findByIdAndHubId(payload.getId(), hubId).isEmpty()) {
            Sensor sensor = new Sensor();
            sensor.setId(payload.getId());
            sensor.setHubId(hubId);
            sensorRepository.save(sensor);
        }
    }

    private void processScenarioRemovedEvent(ScenarioRemovedEventAvro payload, String hubId) {
        String scenarioName = payload.getName();

        Optional<Scenario> optionalScenario = scenarioRepository
                .findByHubIdAndName(hubId, scenarioName);

        Scenario scenario = new Scenario();
        if (optionalScenario.isEmpty()) {
            return;
        }
        scenario.setId(optionalScenario.get().getId());

        List<Long> actionIds = scenarioActionRepository
                .findActionIdsByScenarioId(scenario.getId());
        List<Long> conditionIds = scenarioConditionRepository
                .findConditionIdsByScenarioId(scenario.getId());

        scenarioActionRepository.deleteByScenarioId(scenario.getId());
        scenarioConditionRepository.deleteByScenarioId(scenario.getId());

        actionRepository.deleteAllById(actionIds);
        conditionRepository.deleteAllById(conditionIds);

        scenarioRepository.deleteById(scenario.getId());

    }

    private void processScenarioAddedEvent(ScenarioAddedEventAvro payload, String hubId) {
        String scenarioName = payload.getName();

        Optional<Scenario> optionalScenario = scenarioRepository
                .findByHubIdAndName(hubId, scenarioName);

        Scenario scenario = new Scenario();
        scenario.setHubId(hubId);
        scenario.setName(scenarioName);
        if (optionalScenario.isPresent()) {
            scenario.setId(optionalScenario.get().getId());
        } else {
            scenario = scenarioRepository.save(scenario);
        }

        List<Action> actions = mapToActions(payload.getActions(), scenario.getId());
        List<Sensor> actionSensors = mapToActionSensors(payload.getActions(), hubId);
        List<ScenarioAction> scenarioActions = new ArrayList<>();

        List<Condition> conditions = mapToConditions(payload.getConditions(), scenario.getId());
        List<Sensor> conditionSensors = mapToConditionSensors(payload.getConditions(), hubId);
        List<ScenarioCondition> scenarioConditions = new ArrayList<>();

        try {
            actions = actionRepository.saveAll(actions);
            conditions = conditionRepository.saveAll(conditions);

            for (int i = 0; i < actions.size(); i++) {
                ScenarioAction scenarioAction = new ScenarioAction();
                scenarioAction.setActionId(actions.get(i).getId());
                scenarioAction.setSensorId(actionSensors.get(i).getId());
                scenarioAction.setScenarioId(scenario.getId());
                scenarioActions.add(scenarioAction);
            }

            for (int i = 0; i < conditions.size(); i++) {
                ScenarioCondition scenarioCondition = new ScenarioCondition();
                scenarioCondition.setConditionId(conditions.get(i).getId());
                scenarioCondition.setSensorId(conditionSensors.get(i).getId());
                scenarioCondition.setScenarioId(scenario.getId());
                scenarioConditions.add(scenarioCondition);
            }
            scenarioActionRepository.deleteByScenarioId(scenario.getId());
            scenarioActionRepository.saveAll(scenarioActions);

            scenarioConditionRepository.deleteByScenarioId(scenario.getId());
            scenarioConditionRepository.saveAll(scenarioConditions);

        } catch (DataIntegrityViolationException e) {
            log.error("Ошибка добавления сценария {}\n{}", scenario, e.getStackTrace());
        }
    }

    private List<Condition> mapToConditions(List<ScenarioConditionAvro> scenarioConditionAvroList, Long scenarioId) {
        List<Condition> conditions = new ArrayList<>();
        for (ScenarioConditionAvro conditionAvro : scenarioConditionAvroList) {

            Condition condition = new Condition();
            Optional<ScenarioCondition> optionalScenarioCondition = scenarioConditionRepository
                    .findByScenarioIdAndSensorId(scenarioId, conditionAvro.getSensorId());
            optionalScenarioCondition.ifPresent(scenarioCondition -> condition
                    .setId(scenarioCondition.getConditionId()));

            condition.setOperation(ConditionOperation.valueOf(conditionAvro.getOperation().toString()));
            condition.setType(ConditionType.valueOf(conditionAvro.getType().toString()));
            int value = 0;
            if (conditionAvro.getValue().getClass().equals(Boolean.class)) {
                value = Boolean.parseBoolean(conditionAvro.getValue().toString()) ? 1 : 0;
            } else {
                value = Integer.parseInt(conditionAvro.getValue().toString());
            }

            condition.setValue(value);
            conditions.add(condition);
        }
        return conditions;
    }

    private List<Action> mapToActions(List<DeviceActionAvro> deviceActionAvroList, Long scenarioId) {
        List<Action> actions = new ArrayList<>();
        for (DeviceActionAvro actionAvro : deviceActionAvroList) {
            List<ScenarioAction> scenarioActions = scenarioActionRepository
                    .findByScenarioIdAndSensorId(scenarioId, actionAvro.getSensorId());

            Action action = new Action();

            if (!scenarioActions.isEmpty()) {
                for (ScenarioAction scenarioAction : scenarioActions) {
                    action.setId(scenarioAction.getActionId());
                    action.setType(ActionType.valueOf(actionAvro.getType().toString()));
                    action.setValue(actionAvro.getValue());
                    actions.add(action);
                }
            } else {
                action.setType(ActionType.valueOf(actionAvro.getType().toString()));
                action.setValue(actionAvro.getValue());
                actions.add(action);
            }
        }
        return actions;
    }

    private List<Sensor> mapToActionSensors(List<DeviceActionAvro> deviceActionAvros, String hubId) {
        return deviceActionAvros.stream()
                .map(actionAvro -> {
                    Sensor actionSensor = new Sensor();
                    actionSensor.setId(actionAvro.getSensorId());
                    actionSensor.setHubId(hubId);
                    return actionSensor;
                })
                .toList();
    }

    private List<Sensor> mapToConditionSensors(List<ScenarioConditionAvro> scenarioConditionAvroList, String hubId) {
        return scenarioConditionAvroList.stream()
                .map(conditionAvro -> {
                    Sensor conditionSensor = new Sensor();
                    conditionSensor.setId(conditionAvro.getSensorId());
                    conditionSensor.setHubId(hubId);
                    return conditionSensor;
                })
                .toList();
    }
}