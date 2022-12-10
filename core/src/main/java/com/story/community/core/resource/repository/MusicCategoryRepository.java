package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.story.community.core.resource.entities.music.MusicCategory;

public interface MusicCategoryRepository extends ReactiveMongoRepository<MusicCategory, String> {

}
