package ru.joker.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
public class JavaApplication {
    @Autowired ExerciseRepository exerciseRepository;

    @EventListener(ContextRefreshedEvent.class)
    public void init() {
        exerciseRepository.saveAll(
                Arrays.asList(
                        new ExerciseEntity("String Spring Swing — в чём разница", "ни в чём"),
                        new ExerciseEntity("Какая последняя версия Java", "13.0.0-librca"),
                        new ExerciseEntity("Какая последняя версия Maven?", "Gradle")
                )
        );
    }

    public static void main(String[] args) {
        SpringApplication.run(JavaApplication.class, args);
    }
}
