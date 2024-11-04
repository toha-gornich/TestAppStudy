package com.cl.test.repository;


import com.cl.test.entity.User;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {
    @Query("SELECT users.id, users.first_name, users.last_name, users.email, users.role " +
            "FROM users WHERE users.email = $1")
    Mono<User> findByEmail(String email);

}