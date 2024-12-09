package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.kafka.telemetry.event.SensorEventAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.SensorsSnapshotAvro;

import java.time.Duration;
import java.util.*;

/**
 * Класс AggregationStarter, ответственный за запуск агрегации данных.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AggregationStarter {

    @Value("${topics.sensors}")
    private String sensorsTopic;

    @Value("${bootstrap_server}")
    private String bootstrapServer;

    @Value("${key_deserializer_class}")
    private String keyDeserializerClass;

    @Value("${value_deserializer_class}")
    private String valueDeserializerClass;

    @Value("${key_serializer_class}")
    private String keySerializerClass;

    @Value("${value_serializer_class}")
    private String valueSerializerClass;

    @Value("${topics.snapshots}")
    private String sensorsSnapshotsTopic;

    /**
     * Метод для начала процесса агрегации данных.
     * Подписывается на топики для получения событий от датчиков,
     * формирует снимок их состояния и записывает в кафку.
     */
    Map<String, SensorsSnapshotAvro> snapshots = new HashMap<>();

    public void start() {

        Producer<String, SpecificRecordBase> producer;

        Properties producerConfig = new Properties();
        producerConfig.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);

        producer = new KafkaProducer<>(producerConfig);

        Properties consumerConfig = new Properties();

        consumerConfig.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        consumerConfig.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // обязательные настройки
        consumerConfig.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass);
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClass);

        Consumer<String, SensorEventAvro> consumer = new KafkaConsumer<>(consumerConfig);
        try {
            consumer.subscribe(List.of(sensorsTopic));
            while (true) {
                ConsumerRecords<String, SensorEventAvro> records =
                        consumer.poll(Duration.ofMillis(1000));
                for (ConsumerRecord<String, SensorEventAvro> record : records) {
                    log.info("Получено сообщение из партиции {}, со смещением {}:\n{}\n",
                            record.partition(), record.offset(), record.value());
                    Optional<SensorsSnapshotAvro> optionalSensorsSnapshotAvro = updateState(record.value());
                    optionalSensorsSnapshotAvro
                            .ifPresent(sensorsSnapshotAvro -> {
                                snapshots
                                        .put(record.value().getHubId(), sensorsSnapshotAvro);

                                producer.send(new ProducerRecord<>(sensorsSnapshotsTopic, sensorsSnapshotAvro));

                                log.info("{} отправлено {}", this.getClass().getName(), sensorsSnapshotAvro);
                            });
                }
            }

        } catch (WakeupException ignored) {
        } catch (Exception e) {
            log.error("Ошибка во время обработки событий от датчиков", e);
        } finally {

            try {
                producer.flush();
                consumer.commitSync();

            } finally {
                log.info("Закрываем консьюмер");
                consumer.close();
                log.info("Закрываем продюсер");
                producer.close();
            }
        }
    }

    private Optional<SensorsSnapshotAvro> updateState(SensorEventAvro event) {
        SensorsSnapshotAvro sensorsSnapshotAvro;
        if (snapshots.containsKey(event.getHubId())) {
            sensorsSnapshotAvro = snapshots.get(event.getHubId());
        } else {
            sensorsSnapshotAvro = new SensorsSnapshotAvro();
        }

        SensorStateAvro oldState;
        if (sensorsSnapshotAvro.getSensorsState() != null && sensorsSnapshotAvro.getSensorsState().containsKey(event.getId())) {
            oldState = sensorsSnapshotAvro.getSensorsState().get(event.getId());
            if (oldState.getTimestamp().isAfter(event.getTimestamp())) {
                if (oldState.getData().equals(event.getPayload())) {
                    return Optional.empty();
                }
            }
        }

        SensorStateAvro sensorStateAvro = new SensorStateAvro();
        sensorStateAvro.setTimestamp(event.getTimestamp());
        sensorStateAvro.setData(event.getPayload());

        if (sensorsSnapshotAvro.getSensorsState() == null) {
            sensorsSnapshotAvro.setSensorsState(new HashMap<>());
        }
        sensorsSnapshotAvro.getSensorsState().put(event.getId(), sensorStateAvro);
        sensorsSnapshotAvro.setHubId(event.getHubId());
        sensorsSnapshotAvro.setTimestamp(event.getTimestamp());
        return Optional.of(sensorsSnapshotAvro);
    }
}