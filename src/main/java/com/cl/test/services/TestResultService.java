package com.cl.test.services;

import com.cl.test.entity.*;
import com.cl.test.repository.AnswerRepository;
import com.cl.test.repository.QuestionRepository;
import com.cl.test.repository.TestRepository;
import com.cl.test.repository.TestResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//@Service
//@RequiredArgsConstructor
//public class TestResultService {
//    private final TestResultRepository testResultRepository;
//    private final AnswerRepository answerRepository;
//    private final QuestionRepository questionRepository;
//
//    public Flux<TestResult> getAllTestResults() {
//        return testResultRepository.findAll();
//    }
//
//    public Mono<TestResult> getTestResultById(Long id) {
//        return testResultRepository.findById(id)
//                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Test result not found")));
//    }
//
//    public Flux<TestResult> getTestResults(Long testId) {
//        return testResultRepository.findByTestId(testId);
//    }
//
//    public Flux<TestResult> getUserResults(Long userId) {
//        return testResultRepository.findByUserId(userId);
//    }
//
//    public Mono<TestResult> processTestSubmission(Long testId, TestSubmission submission) {
//        return calculateScore(testId, submission.getUserAnswers())
//                .flatMap(score -> {
//                    TestResult result = new TestResult();
//                    result.setTestId(testId);
//                    result.setUserId(submission.getUserId());
//                    result.setScore(score);
//                    result.setCompletionDate(LocalDateTime.now());
//                    result.setUserAnswers(submission.getUserAnswers());
//                    return testResultRepository.save(result);
//                })
//                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to process submission")));
//    }
//
//    private Mono<Integer> calculateScore(Long testId, Map<Long, List<Long>> userAnswers) {
//        return questionRepository.findByTestId(testId)
//                .flatMap(question -> {
//                    List<Long> selectedAnswers = userAnswers.getOrDefault(question.getId(), List.of());
//                    return answerRepository.findByQuestionId(question.getId())
//                            .filter(Answer::getIsCorrect)
//                            .collectList()
//                            .map(correctAnswers -> scoreQuestion(question, selectedAnswers, correctAnswers));
//                })
//                .reduce(0, Integer::sum);
//    }
//
//    private Integer scoreQuestion(Question question, List<Long> selectedAnswers, List<Answer> correctAnswers) {
//        Set<Long> correctAnswerIds = correctAnswers.stream()
//                .map(Answer::getId)
//                .collect(Collectors.toSet());
//
//        if (question.getType() == QuestionType.SINGLE) {
//            return selectedAnswers.size() == 1 && correctAnswerIds.contains(selectedAnswers.get(0)) ? 1 : 0;
//        } else {
//            Set<Long> selectedAnswerIds = new HashSet<>(selectedAnswers);
//            return correctAnswerIds.equals(selectedAnswerIds) ? 1 : 0;
//        }
//    }
//
//    public Mono<TestResult> updateTestResult(Long id, TestResult testResult) {
//        return testResultRepository.findById(id)
//                .flatMap(existingResult -> {
//                    testResult.setId(id);
//                    return testResultRepository.save(testResult);
//                })
//                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Test result not found")));
//    }
//
//    public Mono<Void> deleteTestResult(Long id) {
//        return testResultRepository.deleteById(id);
//    }
//}

