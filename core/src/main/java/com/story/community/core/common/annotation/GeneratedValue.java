package com.story.community.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.story.community.core.generation.DefaultIdGenerator;
import com.story.community.core.generation.IdGenerator;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
@Documented
@Inherited
public @interface GeneratedValue {
    Class<? extends IdGenerator> generator() default DefaultIdGenerator.class;
}
