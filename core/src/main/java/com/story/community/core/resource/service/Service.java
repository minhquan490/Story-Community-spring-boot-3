package com.story.community.core.resource.service;

import java.util.Collection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface Service<T> {
    Mono<Void> deleteAll();

    Mono<Void> delete(T entity);

    Mono<Void> delete(Collection<T> entities);

    Mono<T> save(T entity);

    Flux<T> saveAll(Collection<T> entities);

    Mono<T> update(T entity);

    Flux<T> update(Collection<T> entities);

    Mono<T> findById(String id);

    Flux<T> findAll();
}
