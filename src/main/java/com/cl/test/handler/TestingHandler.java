package com.cl.test.handler;

import com.cl.test.entity.*;
import com.cl.test.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@RequiredArgsConstructor
public class TestingHandler {
    private final TestService testService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;

    //    private final TestResultService testResultService;

    // Test management handlers
    public Mono<ServerResponse> getAllTests(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(testService.getAllTests(), Test.class);
    }

    public Mono<ServerResponse> getActiveTests(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(testService.getAllTests().filter(Test::isActive), Test.class);
    }

    public Mono<ServerResponse> getTestById(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return testService.getTestById(id)
                .flatMap(test -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(test))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> createTest(ServerRequest request) {
        return request.bodyToMono(Test.class)
                .flatMap(testService::createTest)
                .flatMap(test -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(test));
    }

    public Mono<ServerResponse> updateTest(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(Test.class)
                .flatMap(test -> testService.updateTest(id, test))
                .flatMap(test -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(test))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteTest(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return testService.deleteTest(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> activateTest(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return testService.getTestById(id)
                .flatMap(test -> {
                    test.setActive(true);
                    return testService.updateTest(id, test);
                })
                .flatMap(test -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(test))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    // Question management handlers
    public Mono<ServerResponse> addQuestion(ServerRequest request) {
        Long testId = Long.parseLong(request.pathVariable("testId"));
        return request.bodyToMono(Question.class)
                .flatMap(question -> {
                    question.setTestId(testId);
                    return questionService.createQuestion(question);
                })
                .flatMap(question -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(question));
    }

    public Mono<ServerResponse> updateQuestion(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(Question.class)
                .flatMap(question -> questionService.updateQuestion(id, question))
                .flatMap(question -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(question))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteQuestion(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return questionService.deleteQuestion(id)
                .then(ServerResponse.noContent().build());
    }

    // Answer management handlers
    public Mono<ServerResponse> addAnswer(ServerRequest request) {
        Long questionId = Long.parseLong(request.pathVariable("questionId"));
        return request.bodyToMono(Answer.class)
                .flatMap(answer -> {
                    answer.setQuestionId(questionId);
                    return answerService.createAnswer(answer);
                })
                .flatMap(answer -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(answer));
    }

    public Mono<ServerResponse> updateAnswer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(Answer.class)
                .flatMap(answer -> answerService.updateAnswer(id, answer))
                .flatMap(answer -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(answer))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> deleteAnswer(ServerRequest request) {
        Long id = Long.parseLong(request.pathVariable("id"));
        return answerService.deleteAnswer(id)
                .then(ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse
                .ok()
                .contentType(APPLICATION_JSON)
                .body(userService.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        Long userId = Long.parseLong(request.pathVariable("id"));
        return userService.getUserById(userId)
                .flatMap(user -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(user))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode())
                                .bodyValue(new ErrorResponse(e.getReason())));
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(userService::createUser)
                .flatMap(savedUser -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(APPLICATION_JSON)
                        .bodyValue(savedUser))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode())
                                .bodyValue(new ErrorResponse(e.getReason())));
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        Long userId = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(User.class)
                .flatMap(user -> userService.updateUser(userId, user))
                .flatMap(updatedUser -> ServerResponse
                        .ok()
                        .contentType(APPLICATION_JSON)
                        .bodyValue(updatedUser))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode())
                                .bodyValue(new ErrorResponse(e.getReason())));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        Long userId = Long.parseLong(request.pathVariable("id"));
        return userService.deleteUser(userId)
                .then(ServerResponse.noContent().build());
    }
//    // Test taking and results handlers
//    public Mono<ServerResponse> startTest(ServerRequest request) {
//        Long testId = Long.parseLong(request.pathVariable("id"));
//        return request.bodyToMono(UserTestSession.class)
//                .flatMap(session -> {
//                    session.setTestId(testId);
//                    session.setStartTime(LocalDateTime.now());
//                    return testSessionService.createSession(session);
//                })
//                .flatMap(session -> ServerResponse
//                        .status(HttpStatus.CREATED)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(session));
//    }
//
//    public Mono<ServerResponse> submitTest(ServerRequest request) {
//        Long testId = Long.parseLong(request.pathVariable("id"));
//        return request.bodyToMono(TestSubmission.class)
//                .flatMap(submission -> testResultService.processSubmission(testId, submission))
//                .flatMap(result -> ServerResponse
//                        .ok()
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .bodyValue(result));
//    }
//
//    public Mono<ServerResponse> getTestResults(ServerRequest request) {
//        Long testId = Long.parseLong(request.pathVariable("id"));
//        return ServerResponse
//                .ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(testResultService.getResultsByTestId(testId), TestResult.class);
//    }
//
//    public Mono<ServerResponse> getUserResults(ServerRequest request) {
//        Long userId = Long.parseLong(request.pathVariable("userId"));
//        return ServerResponse
//                .ok()
//                .contentType(MediaType.APPLICATION_JSON)
//                .body(testResultService.getResultsByUserId(userId), TestResult.class);
//    }
}