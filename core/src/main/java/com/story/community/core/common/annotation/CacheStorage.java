package com.story.community.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating entity for creating cache storage for it
 * 
 * @apiNote use for entity only
 * @author hoangquan
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Inherited
public @interface CacheStorage {

    /**
     * Name of cache storage. If not set, class name will be picked.
     */
    String name() default "";
}
