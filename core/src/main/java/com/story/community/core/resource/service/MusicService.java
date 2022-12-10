package com.story.community.core.resource.service;

import com.story.community.core.resource.entities.music.Music;

import reactor.core.publisher.Mono;

public interface MusicService extends Service<Music> {
    Mono<Music> findByName(String name);

}
