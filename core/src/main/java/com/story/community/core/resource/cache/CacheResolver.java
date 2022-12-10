package com.story.community.core.resource.cache;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * A resolver for process a point cut
 * 
 * @author hoangquan
 */
public interface CacheResolver {

    /**
     * Process a point cut for retrieve data from cache storage
     * 
     * @param jointPoint at this point cut
     * @return Mono or Flux as return result of this point cut
     * @throws Throwable throw everything if has problem when retrieve data from
     *                   cache storage
     * @apiNote If return type is not Flux or Mono. Caching process will be skipped
     */
    Object process(ProceedingJoinPoint jointPoint) throws Throwable;
}
