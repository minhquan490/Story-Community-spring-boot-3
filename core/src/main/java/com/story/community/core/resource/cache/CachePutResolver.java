package com.story.community.core.resource.cache;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.story.community.core.common.annotation.CachePut;
import com.story.community.core.common.exception.CacheStorageResolveException;
import com.story.community.core.resource.entities.Entity;
import com.story.community.core.utils.ReflectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j2
public class CachePutResolver implements CacheResolver {
    private final CacheManager cacheManager;

    @Override
    @SuppressWarnings("null")
    public Object process(ProceedingJoinPoint jointPoint) throws Throwable {
        log.info("Begin process put element to cache storage");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        MethodSignature methodSignature = (MethodSignature) jointPoint.getSignature();
        CachePut cachePut = ReflectUtils.getAnnotationOfMethod(methodSignature.getMethod(), CachePut.class);
        Cache cache = cacheManager.getCache(cachePut.cacheName());
        Object key = getKey(jointPoint, cachePut);
        Object cacheValue = findCacheValue(jointPoint);
        if (cacheValue == null || !cacheValue.getClass().getSimpleName().toLowerCase().equals(cachePut.cacheName())) {
            throw new CacheStorageResolveException("Can not resolve [" + cacheValue.getClass().getSimpleName()
                    + "] for cache storage [" + cachePut.cacheName() + "]");
        }
        cache.put(key, cacheValue);
        return jointPoint.proceed();
    }

    /**
     * Extract the key of data from method args
     * 
     * @param joinPoint of target point cut
     * @param cacheable annotation mark on it
     * @return key of data
     */
    private Object getKey(ProceedingJoinPoint joinPoint, CachePut cachePut) {
        Object key = null;
        if (cachePut.keyPosition() < 0) {
            key = joinPoint.getArgs();
        } else {
            key = Arrays.asList(joinPoint.getArgs()).get(cachePut.keyPosition());
        }
        return key;
    }

    /**
     * Find cache value in join point args. And useful in case update or insert
     * operator
     * 
     * @param joinPoint at this point cut
     * @return value need to caching
     */
    private Object findCacheValue(ProceedingJoinPoint joinPoint) {
        for (Object cacheObject : joinPoint.getArgs()) {
            if (cacheObject instanceof Entity) {
                return cacheObject;
            }
        }
        return null;
    }
}
