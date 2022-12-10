package com.story.community.core.common.support;

import java.lang.reflect.Field;

import org.reactivestreams.Publisher;
import org.springframework.data.mongodb.core.mapping.event.ReactiveBeforeConvertCallback;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.story.community.core.common.annotation.GeneratedValue;
import com.story.community.core.common.annotation.HashBy;
import com.story.community.core.generation.DefaultIdGenerator;
import com.story.community.core.generation.IdGenerator;
import com.story.community.core.resource.entities.Entity;
import com.story.community.core.resource.entities.customer.Account;
import com.story.community.core.utils.ReflectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@RequiredArgsConstructor
public class BeforeConvert implements ReactiveBeforeConvertCallback<Entity> {
    private static final String FIELD_PASSWORD_NAME = "password";
    private static final int PASSWORD_LENGTH = 36;

    private final ObjectCreator objectCreator;

    @Override
    public Publisher<Entity> onBeforeConvert(Entity entity, String collection) {
        return Mono.just(entity).doOnNext(e -> {
            if (e.getId() == null) {
                Field idField = ReflectUtils.getIdField(e.getClass());
                GeneratedValue generatedValue = ReflectUtils.getAnnotationOfField(idField, GeneratedValue.class);
                IdGenerator generator;
                if (generatedValue == null) {
                    generator = new DefaultIdGenerator();
                } else {
                    generator = objectCreator.newInstance(generatedValue.generator());
                }
                e.setId(generator.generate());
            }
            Account account = castToAccount(e);
            if (account != null) {
                HashBy hashBy = ReflectUtils.getAnnotationOfField(
                        ReflectUtils.getFieldOfClass(account.getClass(), FIELD_PASSWORD_NAME),
                        HashBy.class);
                PasswordEncoder encoder = objectCreator.newInstance(hashBy.encoder());
                account.setPassword(encoder.encode(account.getPassword()));
            }
            log.info("Ready convert entity [{}] with id [{}] to save into [{}]",
                    e.getClass().getSimpleName(),
                    e.getId(),
                    collection);
        });
    }

    private Account castToAccount(Entity entity) {
        boolean isAccount = entity instanceof Account;
        if (isAccount) {
            Account account = (Account) entity;
            if (account.getPassword() != null && account.getPassword().length() <= PASSWORD_LENGTH) {
                return account;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
