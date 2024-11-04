package com.cl.test.services;


import com.cl.test.entity.Answer;
import com.cl.test.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;

    public Flux<Answer> getAllAnswers() {
        return answerRepository.findAll();
    }

    public Mono<Answer> getAnswerById(Long id) {
        return answerRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found")));
    }

    public Flux<Answer> getAnswersByQuestionId(Long questionId) {
        return answerRepository.findByQuestionId(questionId);
    }

    public Mono<Answer> createAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    public Mono<Answer> updateAnswer(Long id, Answer answer) {
        return answerRepository.findById(id)
                .flatMap(existingAnswer -> {
                    answer.setId(id);
                    return answerRepository.save(answer);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found")));
    }

    public Mono<Void> deleteAnswer(Long id) {
        return answerRepository.deleteById(id);
    }

    public Flux<Answer> createAnswers(Flux<Answer> answers) {
        return answers.flatMap(answerRepository::save);
    }

    public Mono<Boolean> isAnswerCorrect(Long answerId) {
        return answerRepository.findById(answerId)
                .map(Answer::isCorrect)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer not found")));
    }
}