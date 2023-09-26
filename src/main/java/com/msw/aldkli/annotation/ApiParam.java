package com.msw.aldkli.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ElementType.METHOD})
public @interface ApiParam {
    String param();

    String description() default "";

    String example() default "";
}
