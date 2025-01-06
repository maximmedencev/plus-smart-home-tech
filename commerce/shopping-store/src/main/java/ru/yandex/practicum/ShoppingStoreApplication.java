package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ShoppingStoreApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ShoppingStoreApplication.class, args);
    }
}