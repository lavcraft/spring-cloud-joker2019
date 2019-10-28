package ru.joker.demo;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.CollectionSecondPass;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sprincloud.common.Exercise;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class JavaController {
    private final ExerciseRepository exerciseRepository;

    @GetMapping("/random")
    public List<ExerciseEntity> random(@RequestParam(defaultValue = "2") int limit) {
        List<ExerciseEntity> all = exerciseRepository.findAll();
        Collections
                .shuffle(all);

        return all.subList(0, limit);
    }
}
