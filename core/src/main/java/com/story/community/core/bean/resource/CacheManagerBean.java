package com.story.community.core.bean.resource;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.story.community.core.common.Constant;
import com.story.community.core.resource.cache.ResourceCacheManager;

@Configuration
public class CacheManagerBean {

    @Bean
    CacheManager cacheManager() {
		return new ResourceCacheManager(Constant.MONGO_ENTITIES_PACKAGE_STRING);
    }
}
