package com.story.community.core.common.support;

import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeSaveCallback;

import com.story.community.core.resource.entities.Entity;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
public class BeforeSaving implements ReactiveBeforeSaveCallback<Entity> {

    @Override
    public Publisher<Entity> onBeforeSave(Entity entity, Document document, String collection) {
        log.info("Ready saving entity [{}] with id [{}] to collection [{}]",
                entity.getClass().getSimpleName(),
                entity.getId(),
                collection);
        return Mono.just(entity);
    }

}
