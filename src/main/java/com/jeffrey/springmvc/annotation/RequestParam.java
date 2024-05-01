package com.jeffrey.springmvc.annotation;

import java.lang.annotation.*;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-05-01 14:33
 * @description:
 * RequestParam 注解标注在目标方法的参数上，表示对应的http请求参数
 **/
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
}
