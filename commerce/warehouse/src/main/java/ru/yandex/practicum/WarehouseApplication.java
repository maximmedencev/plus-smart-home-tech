package ru.yandex.practicum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class WarehouseApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WarehouseApplication.class, args);
    }
}