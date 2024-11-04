package com.cl.test.repository;

import com.cl.test.entity.Test;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TestRepository extends ReactiveCrudRepository<Test, Long> {
    Flux<Test> findByTutorId(Long tutorId);
    Flux<Test> findByIsActive(boolean isActive);
}