package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.story.community.core.resource.entities.lightnovel.Lightnovel;

public interface LightnovelRepository extends ReactiveMongoRepository<Lightnovel, String> {

}
