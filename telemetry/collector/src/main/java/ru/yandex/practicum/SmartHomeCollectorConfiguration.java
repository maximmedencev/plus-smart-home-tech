package ru.yandex.practicum;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
@ConfigurationProperties
public class SmartHomeCollectorConfiguration {

    @Value("${bootstrap_server}")
    private String bootstrapServer;

    @Value("${key_serializer_class}")
    private String keySerializerClass;

    @Value("${value_serializer_class}")
    private String valueSerializerClass;

    @Bean
    KafkaProducer<String, SpecificRecordBase> kafkaProducer() {
        Producer<String, SpecificRecordBase> producer;

        Properties config = new Properties();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializerClass);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializerClass);

        return new KafkaProducer<>(config);
    }
}