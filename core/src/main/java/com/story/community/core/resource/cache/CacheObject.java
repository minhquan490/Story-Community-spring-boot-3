package com.story.community.core.resource.cache;

import java.lang.annotation.Annotation;

import com.story.community.core.common.annotation.CacheStorage;

/**
 * Holding cache storage name
 * 
 * @author hoangquan
 */
public class CacheObject {

    private String cacheStorageName;
    private Class<? extends Annotation> marker;

    /**
     * Create cache storage with given entity. Entity must be mark by
     * {@link CacheStorage}, if no this entity will be skipped
     * 
     * @param cacheEntity entity for create cache storage
     */
    public CacheObject(Class<?> cacheEntity) {
        this.marker = CacheStorage.class;
        cacheStorageName = resolveCacheStorageName(cacheEntity);
    }

    public String getCacheStorageName() {
        return cacheStorageName;
    }

    private String resolveCacheStorageName(Class<?> cacheEntity) {
        CacheStorage annotation = (CacheStorage) cacheEntity.getAnnotation(marker);
        if (annotation == null) {
			return null;
        }
        return annotation.name().isBlank() ? cacheEntity.getSimpleName().toLowerCase() : annotation.name();
    }
}
