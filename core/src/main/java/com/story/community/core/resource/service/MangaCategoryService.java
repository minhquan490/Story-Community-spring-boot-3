package com.story.community.core.resource.service;

import com.story.community.core.resource.entities.manga.MangaCategory;

import reactor.core.publisher.Mono;

public interface MangaCategoryService extends Service<MangaCategory> {
    Mono<MangaCategory> findByName(String name);
}
