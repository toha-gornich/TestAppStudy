package com.cl.test.repository;

import com.cl.test.entity.Answer;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AnswerRepository extends ReactiveCrudRepository<Answer, Long> {
    // Find answers by question ID
    Flux<Answer> findByQuestionId(Long questionId);

    // Find correct answers for a question
    Flux<Answer> findByQuestionIdAndIsCorrect(Long questionId, Boolean isCorrect);

    // Delete all answers for a question
    Mono<Void> deleteByQuestionId(Long questionId);

    // Count answers for a question
    Mono<Long> countByQuestionId(Long questionId);

    // Count correct answers for a question
    Mono<Long> countByQuestionIdAndIsCorrect(Long questionId, Boolean isCorrect);

    // Custom query to find answers with additional info
    @Query("SELECT a.* FROM answers a " +
            "WHERE a.question_id = :questionId " +
            "ORDER BY a.id ASC")
    Flux<Answer> findAnswersForQuestion(Long questionId);
}