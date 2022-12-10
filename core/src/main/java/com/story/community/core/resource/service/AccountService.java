package com.story.community.core.resource.service;

import com.story.community.core.resource.entities.customer.Account;

import reactor.core.publisher.Mono;

public interface AccountService extends Service<Account> {

    Mono<Account> findByUsername(String username);
}
