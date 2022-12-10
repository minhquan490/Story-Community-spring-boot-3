package com.story.community.core.common.support;

import org.springframework.objenesis.SpringObjenesis;

public class ObjectCreator {
    private final SpringObjenesis springObjenesis;

    public ObjectCreator() {
        this.springObjenesis = new SpringObjenesis();
    }

    public <T> T newInstance(Class<T> clazz) {
        return springObjenesis.newInstance(clazz, true);
    }
}
