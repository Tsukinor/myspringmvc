package com.jeffrey.controller;

import com.jeffrey.entity.Monster;
import com.jeffrey.service.MonsterService;
import com.jeffrey.springmvc.annotation.AutoWired;
import com.jeffrey.springmvc.annotation.Controller;
import com.jeffrey.springmvc.annotation.RequestMapping;
import com.jeffrey.springmvc.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-24 20:10
 * @description:
 **/
@Controller
public class MonsterController {

    @AutoWired
    private MonsterService monsterService;

    @RequestMapping(value = "/list/monster")
    public void listMonster(HttpServletRequest request, HttpServletResponse response){
        //设置编码类型
        response.setContentType("text/html;charset=utf-8");

        //调用monsterService
        List<Monster> monsters = monsterService.listMonster();
        StringBuffer content = new StringBuffer("<h1>妖怪列表</h1>");
        content.append("<table border='1px' width='500px' style='border-collapse:collapse'>");
        for (Monster monster : monsters) {
            content.append("<tr><td>" + monster.getId()
                    + "</td><td>" + monster.getName() + "</td><td>"
                    + monster.getKill() + "</td><td>"
                    + monster.getAge() + "</td></tr>");
        }
        content.append("</table>");


        //获取writer信息
        try {
            PrintWriter writer = response.getWriter();
            writer.write(content.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //通过name返回对应的monster集合
    @RequestMapping(value = "/find/monster")
    public void findMonsterByName(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestParam(value = "name") String paramName){
        //设置编码类型
        response.setContentType("text/html;charset=utf-8");
        System.out.println("接收到的paramName=" + paramName);
        //调用monsterService
        List<Monster> monsters = monsterService.findMonsterByName(paramName);
        StringBuffer content = new StringBuffer("<h1>妖怪列表</h1>");
        content.append("<table border='1px' width='400px' style='border-collapse:collapse'>");
        for (Monster monster : monsters) {
            content.append("<tr><td>" + monster.getId()
                    + "</td><td>" + monster.getName() + "</td><td>"
                    + monster.getKill() + "</td><td>"
                    + monster.getAge() + "</td></tr>");
        }
        content.append("</table>");


        //获取writer信息
        try {
            PrintWriter writer = response.getWriter();
            writer.write(content.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
