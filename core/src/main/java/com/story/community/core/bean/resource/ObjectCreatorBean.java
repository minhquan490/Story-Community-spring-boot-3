package com.story.community.core.bean.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.story.community.core.common.support.ObjectCreator;

@Configuration
public class ObjectCreatorBean {

    @Bean
    ObjectCreator objectCreator() {
        return new ObjectCreator();
    }
}
