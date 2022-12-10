package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.story.community.core.resource.entities.manga.Manga;

public interface MangaRepository extends ReactiveMongoRepository<Manga, String> {

}
