package com.jeffrey.springmvc.handler;

import java.lang.reflect.Method;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-25 19:12
 * @description:
 * HandlerP 对象 用于记录 请求的url 和 控制器的映射方法
 **/
public class HandlerP {

    private String url;
    private Object controller;
    private Method method;

    public HandlerP(String url, Object controller, Method method) {
        this.url = url;
        this.controller = controller;
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "HandlerP{" +
                "url='" + url + '\'' +
                ", controller=" + controller +
                ", method=" + method +
                '}';
    }
}
