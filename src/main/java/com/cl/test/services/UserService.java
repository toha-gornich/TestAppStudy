package com.cl.test.services;

import com.cl.test.entity.User;
import com.cl.test.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Flux<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public Mono<User> createUser(User user) {
        return userRepository.save(user);
    }

    public Mono<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    user.setId(id);
                    return userRepository.save(user);
                })
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")));
    }

    public Mono<Void> deleteUser(Long id) {
        return userRepository.deleteById(id);
    }

}