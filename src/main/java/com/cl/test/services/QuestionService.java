package com.cl.test.services;

import com.cl.test.entity.Question;
import com.cl.test.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;

    public Flux<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Mono<Question> getQuestionById(Long id) {
        return questionRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found")));
    }

    public Flux<Question> getQuestionsByTestId(Long testId) {
        return questionRepository.findByTestId(testId);
    }

    public Mono<Question> createQuestion(Question question) {
        return questionRepository.save(question);
    }

    public Mono<Question> updateQuestion(Long id, Question question) {
        return questionRepository.findById(id)
                .flatMap(existingQuestion -> {
                    question.setId(id);
                    return questionRepository.save(question);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Question not found")));
    }

    public Mono<Void> deleteQuestion(Long id) {
        return questionRepository.deleteById(id);
    }
}
