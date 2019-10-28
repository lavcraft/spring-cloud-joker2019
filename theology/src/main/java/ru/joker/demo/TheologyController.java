package ru.joker.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.sprincloud.common.Exercise;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
public class TheologyController {

    public static final List<Exercise> questions = Arrays.asList(
            new Exercise("Не грешно ли лезть в атом?", "нет"),
            new Exercise("Какая последня версия бога?", "нет")
    );


    @GetMapping("/random")
    public List<Exercise> random(@RequestParam(defaultValue = "2") int limit) {
        Collections.shuffle(questions);
        return questions.subList(0, limit);
    }

}
