package com.jeffrey.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-24 20:15
 * @description:
 * 注解用于表示控制器组件
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {
    String value() default "";

}
