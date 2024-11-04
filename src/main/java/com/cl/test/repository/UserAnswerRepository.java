package com.cl.test.repository;

import com.cl.test.entity.UserAnswer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UserAnswerRepository extends ReactiveCrudRepository<UserAnswer, Long> {
    Flux<UserAnswer> findByTestResultId(Long testResultId);
}