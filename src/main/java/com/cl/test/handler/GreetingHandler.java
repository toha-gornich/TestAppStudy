package com.cl.test.handler;

import com.cl.test.entity.*;
import com.cl.test.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@Component
@RequiredArgsConstructor
public class GreetingHandler {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("Hello World!"));
    }

    public Mono<ServerResponse> users(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue("User Page"));
    }

    public Mono<ServerResponse> admin(ServerRequest request) {
        Flux<User> users = Flux.just(
                new User(1L, "Vasya", "Petrov", "vasya@gmail.com", "1234"),
                new User(2L, "Inna", "Sidorova", "inna@gmail.com", "1234"),
                new User(3L, "Ira", "Ivanova", "ira@gmail.com", "1234")
        );

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
    }

    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userService.getAllUsers(), User.class);
    }

    public Mono<ServerResponse> getUserById(ServerRequest request) {
        Long userId = Long.parseLong(request.pathVariable("id"));
        return userService.getUserById(userId)
                .flatMap(user -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode())
                                .bodyValue(new ErrorResponse(e.getReason())));
    }
//    public Mono<ServerResponse> getUserRoleById(ServerRequest request) {
//        String userId = request.pathVariable("id");
//        Mono<String> userRole = userService.getUserRoleById(userId); // Приклад виклику сервісу
//        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userRole, String.class);
//    }
    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.bodyToMono(User.class)
                .flatMap(userService::createUser)
                .flatMap(savedUser -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(savedUser))
                .onErrorResume(ResponseStatusException.class, e ->
                        ServerResponse.status(e.getStatusCode())
                                .bodyValue(new ErrorResponse(e.getReason())));
    }

    public Mono<ServerResponse> updateUser(ServerRequest request) {
        Long userId = Long.parseLong(request.pathVariable("id"));
        return request.bodyToMono(User.class)
                .flatMap(user -> userService.updateUser(userId, user))
                .flatMap(updatedUser -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
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
    public Mono<ServerResponse> login(ServerRequest request) {
        return request.bodyToMono(AuthRequest.class)
                .flatMap(authRequest -> userService.findByEmail(authRequest.getEmail())
                        .flatMap(user -> {
                            if (passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
                                return ServerResponse.ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new AuthResponse("Login successful"));
                            } else {
                                return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(new AuthResponse("Invalid credentials"));
                            }
                        })
                        .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED)
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(new AuthResponse("User not found")))
                );
    }
}