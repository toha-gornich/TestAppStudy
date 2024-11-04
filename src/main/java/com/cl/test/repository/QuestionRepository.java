package com.cl.test.repository;
import com.cl.test.entity.Question;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface QuestionRepository extends ReactiveCrudRepository<Question, Long> {
    Flux<Question> findByTestId(Long testId);
}
