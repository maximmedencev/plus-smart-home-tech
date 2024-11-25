package ru.yandex.practicum;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.Producer;

public interface CollectorClient {
    Producer<String, SpecificRecordBase> getProducer();
}