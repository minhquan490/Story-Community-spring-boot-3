package com.story.community.core.resource.cache;

import java.util.Collection;

/**
 * Loader for load cache storage specific in classpath
 * 
 * @author hoangquan
 */
public interface CacheObjectLoader {

    /**
     * Load entity mark by {@code @CacheStorage} for create cache storage
     * 
     * @param basePackage base package of cache storage like entity package
     * @return {@code Collection} of {@link CacheObject}
     */
    Collection<CacheObject> loadCacheObject(String basePackage);
}
