package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;

import com.story.community.core.resource.entities.lightnovel.LightnovelCategory;

import reactor.core.publisher.Mono;

public interface LightnovelCategoryRepository extends ReactiveMongoRepository<LightnovelCategory, String> {

    @Query("{'name'} : :#{name}")
    Mono<LightnovelCategory> findByName(@Param("name") String name);
}
