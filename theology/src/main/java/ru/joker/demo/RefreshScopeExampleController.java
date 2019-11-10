package ru.joker.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController @RefreshScope
public class RefreshScopeExampleController {
    @Value("${foo.bar}")
    String foo;

    @GetMapping("/test")
    public String test() {
        return foo;
    }
}
