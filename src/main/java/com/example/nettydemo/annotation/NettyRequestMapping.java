package com.example.nettydemo.annotation;

import com.example.nettydemo.enums.HTTPMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NettyRequestMapping {

    String path() default "";

    HTTPMethod[] method() default HTTPMethod.GET;
}
