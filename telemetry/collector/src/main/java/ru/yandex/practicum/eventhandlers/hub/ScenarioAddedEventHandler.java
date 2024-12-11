package ru.yandex.practicum.eventhandlers.hub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.grpc.telemetry.event.HubEventProto;
import ru.yandex.practicum.kafka.telemetry.event.*;

import java.time.Instant;

@RequiredArgsConstructor
@Component
@Slf4j
public class ScenarioAddedEventHandler implements HubEventHandler {

    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

    @Value("${topics.hubs}")
    private String hubsTopic;

    @Override
    public HubEventProto.PayloadCase getMessageType() {
        return HubEventProto.PayloadCase.SCENARIO_ADDED;
    }


    @Override
    public void handle(HubEventProto event) {
        ScenarioAddedEventAvro scenarioAddedEventAvro = ScenarioAddedEventAvro.newBuilder()
                .setName(event.getScenarioAdded().getName())
                .setActions(event
                        .getScenarioAdded()
                        .getActionsList().stream()
                        .map(actionProto -> DeviceActionAvro.newBuilder()
                                .setSensorId(actionProto.getSensorId())
                                .setType(ActionTypeAvro.valueOf(actionProto.getType().toString()))
                                .build())
                        .toList())
                .setConditions(event
                        .getScenarioAdded()
                        .getConditionsList().stream()
                        .map(conditionProto -> ScenarioConditionAvro.newBuilder()
                                .setSensorId(conditionProto.getSensorId())
                                .setOperation(ConditionOperationAvro.valueOf(conditionProto.getOperation().toString()))
                                .setValue(conditionProto.hasBoolValue() ? conditionProto.getBoolValue() : conditionProto.getIntValue())
                                .build())
                        .toList())
                .build();


        HubEventAvro hubEventAvro = HubEventAvro.newBuilder()
                .setHubId(event.getHubId())
                .setTimestamp(Instant.ofEpochSecond(event.getTimestamp().getSeconds(),
                        event.getTimestamp().getNanos()))
                .setPayload(scenarioAddedEventAvro)
                .build();

        kafkaProducer.send(new ProducerRecord<>(hubsTopic, hubEventAvro));
        log.info("{} отправлено {}", this.getClass().getName(), hubEventAvro);
    }

}

