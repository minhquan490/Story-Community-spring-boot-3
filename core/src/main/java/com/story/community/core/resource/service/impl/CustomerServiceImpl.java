package com.story.community.core.resource.service.impl;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.story.community.core.common.exception.PersistenceException;
import com.story.community.core.common.sercurity.PrincipalObject;
import com.story.community.core.resource.entities.customer.Customer;
import com.story.community.core.resource.repository.CustomerRepository;
import com.story.community.core.resource.service.AccountService;
import com.story.community.core.resource.service.CustomerService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    @Getter(value = AccessLevel.PRIVATE)
    private final CustomerRepository customerRepository;
    private final AccountService accountService;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        AtomicReference<PersistenceException> exceptionHolder = new AtomicReference<>();
        PrincipalObject userDetails = new PrincipalObject();
        accountService
                .findByUsername(username)
                .doOnSuccess(a -> {
                    Customer customer = a.getCustomer();
                    userDetails.setCustomer(customer);
                    userDetails.setId(customer.getId());
                    exceptionHolder.set(null);
                })
                .doOnError(e -> {
                    log.catching(e);
                    exceptionHolder.set(new PersistenceException(e.getMessage(), e));
                })
                .subscribe();
        return exceptionHolder.get() == null ? Mono.just(userDetails) : Mono.error(exceptionHolder.get());
    }

    @Cacheable(cacheNames = "customer", key = "#id")
    @Override
    public Mono<Customer> findById(String id) {
        return customerRepository
                .findById(id)
                .doOnSuccess(c -> log.info("Found customer with id [{}]", id))
                .cache();
    }

    @Override
    public Mono<Void> deleteAll() {
        return getCustomerRepository().deleteAll();
    }

    @Override
    public Mono<Void> delete(Customer entity) {
        return getCustomerRepository().delete(entity);
    }

    @Override
    public Mono<Customer> save(Customer entity) {
        return getCustomerRepository().save(entity);
    }

    @Override
    public Mono<Customer> update(Customer entity) {
        return save(entity);
    }

    @Override
    public Flux<Customer> saveAll(Collection<Customer> entities) {
        return getCustomerRepository().saveAll(entities);
    }

    @Override
    public Flux<Customer> findAll() {
        return getCustomerRepository().findAll();
    }

    @Override
    public Mono<Void> delete(Collection<Customer> entities) {
        return getCustomerRepository().deleteAll(entities);
    }

    @Override
    public Flux<Customer> update(Collection<Customer> entities) {
        return saveAll(entities);
    }
}
