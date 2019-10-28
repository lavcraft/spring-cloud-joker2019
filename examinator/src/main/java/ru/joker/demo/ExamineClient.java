package ru.joker.demo;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.sprincloud.common.Exercise;

import java.util.List;

//@FeignClient("java")
public interface ExamineClient {
    @GetMapping("/random")
    List<Exercise> random(@RequestParam(defaultValue = "2") int limit);
}
