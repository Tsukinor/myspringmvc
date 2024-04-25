package com.jeffrey.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-24 20:18
 * @description:
 * RequestMapping 注解用于指定控制器-方法的映射路径
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
