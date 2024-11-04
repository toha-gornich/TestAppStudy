package com.cl.test.repository;

import com.cl.test.entity.TestResult;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface TestResultRepository extends ReactiveCrudRepository<TestResult, Long> {
    Flux<TestResult> findByTestId(Long testId);
    Flux<TestResult> findByUserId(Long userId);
}