package ru.yandex.practicum;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class SmartHomeCollectorClientConfiguration {
    public static final String BOOTSTRAP_SERVER = "localhost:9092";
    public static final String KEY_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
    public static final String VALUE_SERIALIZER_CLASS = "ru.yandex.practicum.SmartHomeCollectorAvroSerializer";

    @Bean
    CollectorClient getClient() {
        return new CollectorClient() {
            private Producer<String, SpecificRecordBase> producer;

            private void initProducer() {
                Properties config = new Properties();
                config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
                config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KEY_SERIALIZER_CLASS);
                config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, VALUE_SERIALIZER_CLASS);

                producer = new KafkaProducer<>(config);
            }

            @Override
            public Producer<String, SpecificRecordBase> getProducer() {
                if (producer == null) {
                    initProducer();
                }
                return producer;
            }
        };
    }
}