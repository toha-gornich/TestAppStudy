package com.cl.test.controller;

import com.cl.test.entity.Question;
import com.cl.test.services.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping
    public Flux<Question> getAllQuestions() {
        System.out.println("getAllQuestions");
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public Mono<Question> getQuestionById(@PathVariable Long id) {
        return questionService.getQuestionById(id);
    }

    @GetMapping("/test/{testId}")
    public Flux<Question> getQuestionsByTestId(@PathVariable Long testId) {
        return questionService.getQuestionsByTestId(testId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Question> createQuestion(@Valid @RequestBody Question question) {
        return questionService.createQuestion(question);
    }

    @PutMapping("/{id}")
    public Mono<Question> updateQuestion(@PathVariable Long id, @Valid @RequestBody Question question) {
        return questionService.updateQuestion(id, question);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteQuestion(@PathVariable Long id) {
        return questionService.deleteQuestion(id);
    }
}