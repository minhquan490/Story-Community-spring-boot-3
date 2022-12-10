package com.story.community.core.resource.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.story.community.core.resource.entities.music.Music;

public interface MusicRepository extends ReactiveMongoRepository<Music, String> {

}
