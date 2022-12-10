package com.story.community.core.bean.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.story.community.core.common.support.AfterConvert;
import com.story.community.core.common.support.AfterSaving;
import com.story.community.core.common.support.BeforeConvert;
import com.story.community.core.common.support.BeforeSaving;
import com.story.community.core.common.support.ObjectCreator;

@Configuration
public class CallbackBean {

    private ObjectCreator objectCreator;

    @Autowired
    public void setObjectCreator(ObjectCreator objectCreator) {
        this.objectCreator = objectCreator;
    }

    @Bean
    AfterSaving afterSaving() {
        return new AfterSaving();
    }

    @Bean
    AfterConvert afterConvert() {
        return new AfterConvert();
    }

    @Bean
    BeforeConvert beforeConvert() {
        return new BeforeConvert(objectCreator);
    }

    @Bean
    BeforeSaving beforeSaving() {
        return new BeforeSaving();
    }
}
