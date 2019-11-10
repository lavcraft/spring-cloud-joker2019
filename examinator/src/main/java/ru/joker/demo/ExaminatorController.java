package ru.joker.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancerExchangeFilterFunction;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class ExaminatorController {
    private WebClient webClient;

    public ExaminatorController(WebClient.Builder builder,
                                LoadBalancerClient loadBalncerClient) {
        this.webClient = builder
                .filter(new LoadBalancerExchangeFilterFunction(loadBalncerClient))
                .build();
    }


    @PostMapping("/exam")
    public Flux<Object> postExam(@RequestBody Map<String, Integer> examSpec) {

        List<Mono<Section>> collect = examSpec.entrySet()
                .stream()
                .map(entry -> webClient
                        .get()
                        .uri("http://" + entry.getKey() + "/random?limit=" + entry.getValue())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<List<Exercise>>() {
                        })
                        .doOnError(throwable -> log.error("Error when send request", throwable))
                        .onErrorReturn(Collections.emptyList())
                        .map(exercises -> new Section()
                                .setTitle(entry.getKey())
                                .setExercises(exercises))
                )
                .collect(Collectors.toList());

        return Flux
                .zip(collect, Arrays::asList)
                .flatMapIterable(objects -> objects);


    }
}
