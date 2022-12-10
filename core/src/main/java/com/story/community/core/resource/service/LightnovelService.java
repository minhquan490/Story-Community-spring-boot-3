package com.story.community.core.resource.service;

import com.story.community.core.resource.entities.Status;
import com.story.community.core.resource.entities.lightnovel.Lightnovel;
import com.story.community.core.resource.entities.lightnovel.Type;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface LightnovelService extends Service<Lightnovel> {
    Mono<Lightnovel> findByName(String name);

    Flux<Lightnovel> findByType(Type type);

    Flux<Lightnovel> findByStatus(Status status);
}