package ru.yandex.practicum.service;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CollectorService {

    private final KafkaProducer<String, SpecificRecordBase> kafkaProducer;

//    public SensorEvent collectSensorEvent(SensorEvent event, String topic) {
//        switch (event.getType()) {
//            case CLIMATE_SENSOR_EVENT -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            SensorMappers.maptoToClimateSensorAvro((ClimateSensorEvent) event)));
//            case LIGHT_SENSOR_EVENT -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            SensorMappers.maptoToLightSensorAvro((LightSensorEvent) event)));
//            case MOTION_SENSOR_EVENT -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            SensorMappers.maptoToMotionSensorAvro((MotionSensorEvent) event)));
//            case SWITCH_SENSOR_EVENT -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            SensorMappers.mapToSwitchSensorAvro((SwitchSensorEvent) event)));
//            case TEMPERATURE_SENSOR_EVENT -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            SensorMappers.mapToTemperatureSensorAvro((TemperatureSensorEvent) event)));
//        }
//        return event;
//    }
//
//    public HubEvent collectHubEvent(HubEvent event, String topic) {
//        switch (event.getType()) {
//            case DEVICE_ADDED -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            HubMappers.mapToToHubEventAvro((DeviceAddedEvent) event)));
//            case DEVICE_REMOVED -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            HubMappers.mapToToHubEventAvro((DeviceRemovedEvent) event)));
//            case SCENARIO_ADDED -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            HubMappers.mapToHubEventAvro((ScenarioAddedEvent) event)));
//            case SCENARIO_REMOVED -> kafkaProducer
//                    .send(new ProducerRecord<>(topic,
//                            HubMappers.mapToHubEventAvro((ScenarioRemovedEvent) event)));
//        }
//        return event;
//    }
}
