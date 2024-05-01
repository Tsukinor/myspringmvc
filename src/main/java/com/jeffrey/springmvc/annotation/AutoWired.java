package com.jeffrey.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-24 22:15
 * @description:
 *
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoWired {
    String value() default "";
}
