package com.story.community.core.resource.cache;

import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StopWatch;

import com.story.community.core.common.annotation.CacheEvict;
import com.story.community.core.resource.entities.Entity;
import com.story.community.core.utils.ReflectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@RequiredArgsConstructor
@Log4j2
public class CacheEvictResolver implements CacheResolver {
    private static final String LOG_MSG = "Process done in [{}] ms";
    private final CacheManager cacheManager;

    @Pointcut("@annotation(com.story.community.core.common.annotation.CacheEvict)")
    private void cacheEvictPointCut() {
        // Do nothing
    }

    @Around("cacheEvictPointCut()")
    @Override
    @SuppressWarnings("null")
    public Object process(ProceedingJoinPoint jointPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.info("Begin process evict cache storage");
        MethodSignature methodSignature = (MethodSignature) jointPoint.getSignature();
        CacheEvict cacheEvict = ReflectUtils.getAnnotationOfMethod(methodSignature.getMethod(), CacheEvict.class);
        Cache cache = cacheManager.getCache(cacheEvict.cacheName());
        Assert.notNull(cache, "Cache storage for " + cacheEvict.cacheName() + " is not intialize");
        if (cacheEvict.allEntries()) {
            log.debug("Evict all element in cache storage");
            cache.invalidate();
            finishProcessAndLog(log, stopWatch);
            return jointPoint.proceed(jointPoint.getArgs());
        }
        if (cacheEvict.keyPosition() < 0) {
            log.debug("Evict element in cache storage with key [{}]", jointPoint.getArgs());
            cache.evictIfPresent(jointPoint.getArgs());
            finishProcessAndLog(log, stopWatch);
            return jointPoint.proceed();
        } else {
            Object key = jointPoint.getArgs()[cacheEvict.keyPosition()];
            if (key instanceof Entity entity) {
                log.debug("Evict element with id [{}]", entity.getId());
                cache.evictIfPresent(entity.getId());
                finishProcessAndLog(log, stopWatch);
                return jointPoint.proceed();
            } else {
                log.debug("Evict element with key [{}]", key);
                cache.evictIfPresent(key);
                finishProcessAndLog(log, stopWatch);
                return jointPoint.proceed();
            }
        }
    }

    private void finishProcessAndLog(Logger log, StopWatch stopWatch) {
        stopWatch.stop();
        log.info(LOG_MSG, stopWatch.getTotalTimeMillis());
    }
}
