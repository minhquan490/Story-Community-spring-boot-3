package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.story.community.core.resource.entities.lightnovel.LightnovelChapter;

public interface LightnovelChapterRepository extends ReactiveMongoRepository<LightnovelChapter, String> {

}
