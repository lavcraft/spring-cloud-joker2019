package ru.joker.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ExerciseRepository extends JpaRepository<ExerciseEntity, String> {
}
