package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.story.community.core.resource.entities.manga.MangaCategory;

public interface MangaCategoryRepository extends ReactiveMongoRepository<MangaCategory, String> {

}
