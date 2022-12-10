package com.story.community.core.resource.service;

import com.story.community.core.resource.entities.Status;
import com.story.community.core.resource.entities.manga.Manga;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MangaService extends Service<Manga> {
    Mono<Manga> findByName(String name);

    Flux<Manga> findByStatus(Status status);

    Flux<Manga> findByHot(boolean isHot);
}
