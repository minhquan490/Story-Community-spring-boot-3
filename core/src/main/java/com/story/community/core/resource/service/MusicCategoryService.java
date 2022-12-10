package com.story.community.core.resource.service;

import com.story.community.core.resource.entities.music.MusicCategory;

import reactor.core.publisher.Mono;

public interface MusicCategoryService extends Service<MusicCategory> {
    Mono<MusicCategory> findByName(String name);
}
