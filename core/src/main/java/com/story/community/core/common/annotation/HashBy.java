package com.story.community.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Documented
@Inherited
public @interface HashBy {

    /**
     * Specify {@code encoder} for use. Default {@link BCryptPasswordEncoder}
     */
    Class<? extends PasswordEncoder> encoder() default BCryptPasswordEncoder.class;
}
