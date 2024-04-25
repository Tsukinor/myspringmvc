package com.jeffrey.controller;

import com.jeffrey.springmvc.annotation.Controller;
import com.jeffrey.springmvc.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-25 21:18
 * @description:
 **/
@Controller
public class OrderController {

    @RequestMapping(value = "/order/list")
    public void list(HttpServletRequest request, HttpServletResponse response){
        //设置编码类型
        response.setContentType("text/html;charset=utf-8");
        //获取writer信息
        try {
            PrintWriter writer = response.getWriter();
            writer.write("<h1>Order</h1>");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
