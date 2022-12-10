package com.story.community.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that the result of invoking a method must be cached.
 * 
 * @apiNote use only in insert or update service
 * @author hoangquan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
@Documented
public @interface CachePut {

    /**
     * Names of the caches in which method invocation results are stored.
     */
    String cacheName();

    /**
     * Position of cache key in method args. If negative the resolver will take all
     * method args as key.
     */
    int keyPosition() default -1;
}
