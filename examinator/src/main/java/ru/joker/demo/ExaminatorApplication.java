package ru.joker.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ExaminatorProperties.class)
public class ExaminatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExaminatorApplication.class, args);
    }
}
