package ru.joker.demo;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sprincloud.common.Exercise;
import ru.sprincloud.common.Section;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ExaminatorController {
    private final ExaminatorProperties examinatorProperties;
    private final WebClient.Builder    webClientMap;

    public ExaminatorController(ExaminatorProperties examinatorProperties,
                                @LoadBalanced WebClient.Builder builder) {
        this.examinatorProperties = examinatorProperties;
        this.webClientMap = builder;
    }

    @PostMapping("/exam")
    public Flux<Object> postExam(@RequestBody Map<String, Integer> examSpec) {

        List<Mono<Section>> collect = examSpec.entrySet()
                .stream()
                .map(entry -> webClientMap
                        .build()
                        .get()
                        .uri("http://" + entry.getKey() + "/random?limit=" + entry.getValue())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Exercise>>() {
                        })
                        .map(exercises -> new Section()
                                .setTitle(entry.getKey())
                                .setExercises(exercises))
                )
                .collect(Collectors.toList());

        return Flux
                .zip(collect, objects -> Arrays.asList(objects))
                .flatMapIterable(objects -> objects);


    }
}
