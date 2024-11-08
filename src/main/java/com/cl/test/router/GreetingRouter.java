package com.cl.test.router;
import com.cl.test.handler.GreetingHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class GreetingRouter {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(GreetingHandler handler) {
        return RouterFunctions
                .route(GET("/"), handler::hello)
//                .andRoute(GET("/admin").and(accept(APPLICATION_JSON)), handler::admin)
                .andRoute(GET("/users").and(accept(APPLICATION_JSON)), handler::getAllUsers)
                .andRoute(GET("/users/{id}").and(accept(APPLICATION_JSON)), handler::getUserById)
                .andRoute(POST("/login").and(accept(APPLICATION_JSON)), handler::login)
                .andRoute(POST("/register").and(accept(APPLICATION_JSON)), handler::createUser)
//                .andRoute(POST("/users").and(accept(APPLICATION_JSON)), handler::createUser)
                .andRoute(PUT("/users/{id}").and(accept(APPLICATION_JSON)), handler::updateUser)
                .andRoute(DELETE("/users/{id}"), handler::deleteUser);
    }
}