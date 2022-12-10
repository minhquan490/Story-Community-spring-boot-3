package com.story.community.core.common.support;

import org.bson.Document;
import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.mapping.event.ReactiveAfterConvertCallback;

import com.story.community.core.resource.entities.Entity;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
public class AfterConvert implements ReactiveAfterConvertCallback<Entity> {

    @Override
    public Publisher<Entity> onAfterConvert(Entity entity, Document document, String collection) {
        log.info("Convert entity [{}] with id [{}] to docuent for save into [{}] complete",
                entity.getClass().getSimpleName(),
                entity.getId(),
                collection);
        return Mono.just(entity);
    }

}
