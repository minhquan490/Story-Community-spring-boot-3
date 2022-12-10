package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.story.community.core.resource.entities.manga.MangaChapter;

public interface MangaChapterRepository extends ReactiveMongoRepository<MangaChapter, String> {

}
