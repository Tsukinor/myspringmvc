package com.jeffrey.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-25 22:43
 * @description:
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default "";
}
