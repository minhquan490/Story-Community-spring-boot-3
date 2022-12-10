package com.story.community.core.resource.cache;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import com.story.community.core.common.support.Resource;
import com.story.community.core.utils.ResourceUtils;

import lombok.RequiredArgsConstructor;

/**
 * Simple implementation for {@link CacheObjectLoader}
 * 
 * @author hoangquan
 */
@RequiredArgsConstructor
public class DefaultCacheObjectLoader implements CacheObjectLoader {

    private final ClassLoader classLoader;

    @Override
    public Collection<CacheObject> loadCacheObject(String basePackage) {
        Resource<Set<Class<?>>> resource = ResourceUtils.getClassFromClasspath(classLoader, basePackage);
        Collection<CacheObject> cacheObjects = new LinkedList<>();
        resource.getValue().forEach(c -> {
            CacheObject cacheObject = new CacheObject(c);
            cacheObjects.add(cacheObject);
        });
        return cacheObjects;
    }
}
