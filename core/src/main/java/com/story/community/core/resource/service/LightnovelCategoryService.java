package com.story.community.core.resource.service;

import com.story.community.core.resource.entities.lightnovel.LightnovelCategory;

import reactor.core.publisher.Mono;

public interface LightnovelCategoryService extends Service<LightnovelCategory> {

    Mono<LightnovelCategory> findByName(String categoryName);
}
