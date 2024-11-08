package com.cl.test.services;

import com.cl.test.entity.User;
import com.cl.test.repository.RoleRepository;
import com.cl.test.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements ReactiveUserDetailsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return userRepository.findByEmail(email)
                .flatMap(this::addAuthorities)
                .switchIfEmpty(Mono.empty())
                .doOnError(error -> log.error("Error finding user by email {}: {}", email, error.getMessage()));
    }

    @PostConstruct
    public void initDatabase() {
        // Check if admin user exists
        if (userRepository.findByEmail("2222@gmail.com").blockOptional().isEmpty()) {
            // Create admin user
            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Admin");
            admin.setEmail("2222@gmail.com");
            admin.setPassword(passwordEncoder.encode("2222"));
            admin.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));
            userRepository.save(admin).block();


        }

        // Check if user exists
        if (userRepository.findByEmail("1111@gmail.com").blockOptional().isEmpty()) {
            // Create user
            User user = new User();
            user.setFirstName("John");
            user.setLastName("Doe");
            user.setEmail("1111@gmail.com");
            user.setPassword(passwordEncoder.encode("1111"));
            user.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

            userRepository.save(user).block();

        }
    }
    private Mono<UserDetails> addAuthorities(User user) {
        return roleRepository.findByUserId(user.getId())
                .flatMapMany(role -> Mono.just(new SimpleGrantedAuthority(role.getName())))
                .collectList()
                .map(authorities -> {
                    user.setAuthorities(authorities);
                    return (UserDetails) user;
                })
                .switchIfEmpty(Mono.just((UserDetails) user));
    }

    public Flux<UserDetails> getAllUsers() {
        return userRepository.findAll()
                .flatMap(this::addAuthorities)
                .doOnError(error ->
                        log.error("Error fetching all users: {}", error.getMessage())
                );
    }
    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User with ID %d not found", id))))
                .doOnError(error -> log.error("Error fetching user by ID {}: {}", id, error.getMessage()));
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmailWithDetails(email)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User with email %s not found", email))))
                .doOnError(error -> log.error("Error finding user by email {}: {}", email, error.getMessage()));
    }


    public Mono<User> createUser(User user) {
        return validateNewUser(user)
                .flatMap(validUser -> {
                    validUser.setPassword(passwordEncoder.encode(validUser.getPassword()));
                    validUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
                    return userRepository.save(validUser);
                })
                .doOnSuccess(savedUser -> log.info("Created new user with ID: {}", savedUser.getId()))
                .doOnError(error -> log.error("Error creating user: {}", error.getMessage()));
    }



    public Mono<User> updateUser(Long id, User user) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User with ID %d not found", id))))
                .flatMap(existingUser -> validateUpdateUser(id, user))
                .flatMap(validUser -> {
                    if (validUser.getPassword() != null && !validUser.getPassword().isEmpty()) {
                        validUser.setPassword(passwordEncoder.encode(validUser.getPassword()));
                    }
                    validUser.setId(id);
                    return userRepository.save(validUser);
                })
                .doOnSuccess(updatedUser -> log.info("Updated user with ID: {}", updatedUser.getId()))
                .doOnError(error -> log.error("Error updating user with ID {}: {}", id, error.getMessage()));
    }

    /**
     * Видалити користувача
     */
    public Mono<Void> deleteUser(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("User with ID %d not found", id))))
                .flatMap(user -> userRepository.deleteById(id))
                .doOnSuccess(void1 -> log.info("Deleted user with ID: {}", id))
                .doOnError(error -> log.error("Error deleting user with ID {}: {}", id, error.getMessage()));
    }


    private Mono<User> validateNewUser(User user) {
        return Mono.just(user)
                .flatMap(u -> {
                    if (u.getEmail() == null || u.getEmail().isEmpty()) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Email is required"));
                    }
                    if (!isValidEmail(u.getEmail())) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Invalid email format"));
                    }
                    if (u.getPassword() == null || u.getPassword().isEmpty()) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Password is required"));
                    }
                    if (u.getFirstName() == null || u.getFirstName().isEmpty()) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "First name is required"));
                    }
                    if (u.getLastName() == null || u.getLastName().isEmpty()) {
                        return Mono.error(new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Last name is required"));
                    }
                    return userRepository.findByEmailWithDetails(u.getEmail())
                            .flatMap(existingUser -> Mono.error(new ResponseStatusException(
                                    HttpStatus.CONFLICT, "Email already exists")))
                            .then(Mono.just(u));
                });
    }


    private Mono<User> validateUpdateUser(Long id, User user) {
        return Mono.just(user)
                .flatMap(u -> {
                    if (u.getEmail() != null) {
                        if (u.getEmail().isEmpty()) {
                            return Mono.error(new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST, "Email cannot be empty"));
                        }
                        if (!isValidEmail(u.getEmail())) {
                            return Mono.error(new ResponseStatusException(
                                    HttpStatus.BAD_REQUEST, "Invalid email format"));
                        }
                        return userRepository.findByEmailWithDetails(u.getEmail())
                                .filter(existingUser -> !existingUser.getId().equals(id))
                                .flatMap(existingUser -> Mono.error(new ResponseStatusException(
                                        HttpStatus.CONFLICT, "Email already exists")))
                                .then(Mono.just(u));
                    }
                    return Mono.just(u);
                });
    }


    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}

