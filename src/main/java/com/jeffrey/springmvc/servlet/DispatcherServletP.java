package com.jeffrey.springmvc.servlet;

import com.jeffrey.springmvc.context.WebApplicationContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-23 22:20
 * @description:
 * 1.DispatcherServletP充当前端控制器（原生DispatcherServlet）
 * 2.本质是个servlet，继承httpServlet
 *
 **/


public class DispatcherServletP extends HttpServlet {

    @Override
    public void init() throws ServletException {
        WebApplicationContext webApplicationContext = new WebApplicationContext();
        webApplicationContext.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
        System.out.println("DispatcherServletP------doGet---被调用");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DispatcherServletP-----doPost---被调用");
    }
}
