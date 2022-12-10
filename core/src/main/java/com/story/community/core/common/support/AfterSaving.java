package com.story.community.core.common.support;

import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.mapping.event.ReactiveAfterConvertCallback;

import com.story.community.core.resource.entities.Entity;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
public class AfterSaving implements ReactiveAfterConvertCallback<Entity> {

    @Override
    public Publisher<Entity> onAfterConvert(Entity entity, Document document, String collection) {
        log.info("Save entity [{}] with id [{}] into collection [{}] complete",
                entity.getClass().getSimpleName(),
                entity.getId(),
                collection);
        return Mono.just(entity);
    }

}
