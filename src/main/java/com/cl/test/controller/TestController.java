package com.cl.test.controller;

import com.cl.test.entity.Test;
import com.cl.test.services.TestService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @GetMapping
    public Flux<Test> getAllTests() {
        logger.info("getAllQuestions");
        return testService.getAllTests();
    }

    @GetMapping("/{id}")
    public Mono<Test> getTestById(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Test> createTest(@Valid @RequestBody Test test) {
        return testService.createTest(test);
    }

    @PutMapping("/{id}")
    public Mono<Test> updateTest(@PathVariable Long id, @Valid @RequestBody Test test) {
        return testService.updateTest(id, test);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteTest(@PathVariable Long id) {
        return testService.deleteTest(id);
    }
}
