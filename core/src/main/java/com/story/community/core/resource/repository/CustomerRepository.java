package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import com.story.community.core.resource.entities.customer.Account;
import com.story.community.core.resource.entities.customer.Customer;

import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {

    @Query("{'account' : :#{account}}")
    Mono<Customer> findByAccount(@Param("account") Account account);
}
