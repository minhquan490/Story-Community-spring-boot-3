package com.story.community.core.resource.service.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.story.community.core.common.annotation.Cacheable;
import com.story.community.core.resource.entities.customer.Account;
import com.story.community.core.resource.repository.AccountRepository;
import com.story.community.core.resource.service.AccountService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	@Getter(value = AccessLevel.PRIVATE)
	private final AccountRepository accountRepository;

	@Override
	@Cacheable(cacheName = "account", keyPosition = 0)
	public Mono<Account> findByUsername(String username) {
		return accountRepository.findByUsername(username)
				.doOnSuccess(c -> log.info("Found account with username [{}]", username)).cache();
	}

	@Override
	@Cacheable(cacheName = "account", keyPosition = 0)
	public Mono<Account> findById(String accountId) {
		return getAccountRepository().findById(accountId);
	}

	@Override
	@Transactional
	public Mono<Void> deleteAll() {
		return getAccountRepository().deleteAll();
	}

	@Override
	@Transactional
	public Mono<Void> delete(Account entity) {
		return getAccountRepository().delete(entity);
	}

	@Override
	@Transactional
	public Mono<Account> save(Account entity) {
		return getAccountRepository().save(entity);
	}

	@Override
	@Transactional
	public Mono<Account> update(Account entity) {
		return save(entity);
	}

	@Override
	@Transactional
	public Flux<Account> saveAll(Collection<Account> entities) {
		return getAccountRepository().saveAll(entities);
	}

	@Override
	@Cacheable(cacheName = "account")
	public Flux<Account> findAll() {
		return getAccountRepository().findAll();
	}

	@Override
	public Mono<Void> delete(Collection<Account> entities) {
		return getAccountRepository().deleteAll(entities);
	}

	@Override
	@Transactional
	public Flux<Account> update(Collection<Account> entities) {
		return saveAll(entities);
	}

}
