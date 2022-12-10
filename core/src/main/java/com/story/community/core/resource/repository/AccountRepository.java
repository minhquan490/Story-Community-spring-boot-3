package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import com.story.community.core.resource.entities.customer.Account;

import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {

    @Query("{'username' : :#{username}}")
    Mono<Account> findByUsername(@Param("username") String username);
}
