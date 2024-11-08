package com.cl.test.router;

import com.cl.test.handler.TestingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class TestingRouter {

    @Bean
    public RouterFunction<ServerResponse> route(TestingHandler testingHandler) {
        return RouterFunctions
                // Test management routes
                .route(GET("/tests").and(accept(APPLICATION_JSON)), testingHandler::getAllTests)
                .andRoute(GET("/tests/active").and(accept(APPLICATION_JSON)), testingHandler::getActiveTests)
                .andRoute(GET("/tests/{id}").and(accept(APPLICATION_JSON)), testingHandler::getTestById)
                .andRoute(POST("/tests").and(accept(APPLICATION_JSON)), testingHandler::createTest)
                .andRoute(PUT("/tests/{id}").and(accept(APPLICATION_JSON)), testingHandler::updateTest)
                .andRoute(DELETE("/tests/{id}").and(accept(APPLICATION_JSON)), testingHandler::deleteTest)
                .andRoute(PUT("/tests/{id}/activate").and(accept(APPLICATION_JSON)), testingHandler::activateTest)

                // Question management routes
                .andRoute(POST("/tests/{testId}/questions").and(accept(APPLICATION_JSON)), testingHandler::addQuestion)
                .andRoute(PUT("/questions/{id}").and(accept(APPLICATION_JSON)), testingHandler::updateQuestion)
                .andRoute(DELETE("/questions/{id}").and(accept(APPLICATION_JSON)), testingHandler::deleteQuestion)

                // Answer management routes
                .andRoute(POST("/questions/{questionId}/answers").and(accept(APPLICATION_JSON)), testingHandler::addAnswer)
                .andRoute(PUT("/answers/{id}").and(accept(APPLICATION_JSON)), testingHandler::updateAnswer)
                .andRoute(DELETE("/answers/{id}").and(accept(APPLICATION_JSON)), testingHandler::deleteAnswer);


//                // Test taking and results routes
//                .andRoute(POST("/api/tests/{id}/start").and(accept(APPLICATION_JSON)), testingHandler::startTest)
//                .andRoute(POST("/api/tests/{id}/submit").and(accept(APPLICATION_JSON)), testingHandler::submitTest)
//                .andRoute(GET("/api/tests/{id}/results").and(accept(APPLICATION_JSON)), testingHandler::getTestResults)
//                .andRoute(GET("/api/users/{userId}/results").and(accept(APPLICATION_JSON)), testingHandler::getUserResults);
    }
}