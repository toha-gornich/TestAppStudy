package com.cl.test.repository;


import com.cl.test.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

    @Query("SELECT * FROM users WHERE email = :email")
    Mono<User> findByEmail(String email);

    @Query("SELECT u.id, u.first_name, u.last_name, u.email, u.password FROM users u WHERE u.email = :email")
    Mono<User> findByEmailWithDetails(String email);

    Flux<User> findAll();
}