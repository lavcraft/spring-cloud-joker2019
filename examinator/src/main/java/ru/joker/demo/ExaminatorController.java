package ru.joker.demo;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.sprincloud.common.Exercise;
import ru.sprincloud.common.Section;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class ExaminatorController {
    private final ExaminatorProperties   examinatorProperties;
    private final Map<String, WebClient> webClientMap;

    public ExaminatorController(ExaminatorProperties examinatorProperties,
                                WebClient.Builder webClientBuilder) {
        this.examinatorProperties = examinatorProperties;

        webClientMap = examinatorProperties
                .getUrls()
                .entrySet()
                .stream()
                .map(stringStringEntry ->
                        Map.entry(stringStringEntry.getKey(),
                                webClientBuilder
                                        .baseUrl(stringStringEntry.getValue())
                                        .build()
                        )
                )
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue
                ));
    }

    @PostMapping("/exam")
    public Flux<Object> postExam(@RequestBody Map<String, Integer> examSpec) {

        List<Mono<Section>> collect = examSpec.entrySet()
                .stream()
                .map(entry -> webClientMap.get(entry.getKey())
                        .get()
                        .uri("/random?limit=" + entry.getValue())
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
