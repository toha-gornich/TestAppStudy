package com.cl.test.services;

import com.cl.test.entity.Test;
import com.cl.test.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;

    public Flux<Test> getAllTests() {
        return testRepository.findAll();
    }

    public Mono<Test> getTestById(Long id) {
        return testRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Test not found")));
    }

    public Mono<Test> createTest(Test test) {
        return testRepository.save(test);
    }

    public Mono<Test> updateTest(Long id, Test test) {
        return testRepository.findById(id)
                .flatMap(existingTest -> {
                    test.setId(id);
                    return testRepository.save(test);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Test not found")));
    }

    public Mono<Void> deleteTest(Long id) {
        return testRepository.deleteById(id);
    }
}