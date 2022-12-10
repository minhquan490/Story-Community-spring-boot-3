package com.story.community.core.resource.cache;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import com.story.community.core.common.Constant;
import com.story.community.core.common.annotation.Cacheable;
import com.story.community.core.resource.entities.Entity;
import com.story.community.core.utils.ReflectUtils;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The Aspect use for resolve annotation {@code @Cacheable}. Play with caching
 * storage for get data from it if data is available
 * 
 * @author hoangquan
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CacheableResolver implements CacheResolver {
    private final CacheManager cacheManager;

    @Pointcut("@annotation(com.story.community.core.common.annotation.Cacheable)")
    private void cacheablePointCut() {
        // Point cut so do nothing
    }

    @Around("cacheablePointCut()")
    @Override
    public Object process(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> returnType = signature.getReturnType();
        Cacheable cacheable = ReflectUtils.getAnnotationOfMethod(signature.getMethod(), Cacheable.class);
        if (returnType.equals(Mono.class)) {
            return monoCacheResolve(joinPoint, returnType, cacheManager.getCache(cacheable.cacheName()));
        }
        if (returnType.equals(Flux.class)) {
            return fluxCacheResolve(joinPoint, returnType, cacheManager.getCache(cacheable.cacheName()));
        }
        return joinPoint.proceed();
    }

    /**
     * Put the value to cache storage
     * 
     * @param cache for put value to it
     * @param key   key of data
     * @param c     data
     */
    private void cacheData(Cache cache, Object key, Object c) {
        cache.put(key, c);
    }

    /**
     * Extract the key of data from method args
     * 
     * @param joinPoint of target point cut
     * @param cacheable annotation mark on it
     * @return key of data
     */
    private Object getKey(ProceedingJoinPoint joinPoint, Cacheable cacheable) {
        Object key = null;
        if (cacheable.keyPosition() < 0) {
            key = joinPoint.getArgs();
        } else {
            key = Arrays.asList(joinPoint.getArgs()).get(cacheable.keyPosition());
        }
        return key;
    }

    /**
     * Get value in cache storage with given key
     * 
     * @param cache      cache storage
     * @param key        key of cache
     * @param returnType return type of target method
     * @return value of given key
     */
    private Object getCacheValue(Cache cache, Object key, Class<?> returnType) {
        Class<?> type = ReflectUtils.extractClassFromGeneric(returnType);
        if (key == null) {
            return cache.get(Constant.KEY_GET_ALL_VALUE_CACHE, type);
        }
        if (key instanceof Entity entity) {
            return cache.get(entity.getId(), type);
        } else {
            return cache.get(key, type);
        }
    }

    @SuppressWarnings("unchecked")
    private Mono<?> monoCacheResolve(ProceedingJoinPoint joinPoint, Class<?> returnType, Cache cache)
            throws Throwable {
        MethodSignature signature = getMethodSignature(joinPoint);
        Cacheable cacheable = getCacheableAnnotation(signature.getMethod());
        Object key = getKey(joinPoint, cacheable);
        Object value = getCacheValue(cache, key, returnType);
        if (value == null) {
            return ((Mono<Object>) joinPoint.proceed()).doOnSuccess(c -> cacheData(cache, key, c));
        }
        return Mono.just(value);
    }

    @SuppressWarnings("unchecked")
    private Flux<?> fluxCacheResolve(ProceedingJoinPoint joinPoint, Class<?> returnType, Cache cache)
            throws Throwable {
        MethodSignature signature = getMethodSignature(joinPoint);
        Cacheable cacheable = getCacheableAnnotation(signature.getMethod());
        Object key = getKey(joinPoint, cacheable);
        Object value = getCacheValue(cache, key, returnType);
        if (value == null) {
            return ((Flux<Object>) joinPoint.proceed()).map(c -> {
                cacheData(cache, key, c);
                return c;
            });
        }
        return Flux.fromIterable((Iterable<?>) value);
    }

    private MethodSignature getMethodSignature(ProceedingJoinPoint joinPoint) {
        return (MethodSignature) joinPoint.getSignature();
    }

    private Cacheable getCacheableAnnotation(Method method) {
        return ReflectUtils.getAnnotationOfMethod(method, Cacheable.class);
    }
}
