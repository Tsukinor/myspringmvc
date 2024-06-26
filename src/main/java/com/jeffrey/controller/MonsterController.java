package com.jeffrey.controller;

import com.jeffrey.entity.Monster;
import com.jeffrey.service.MonsterService;
import com.jeffrey.springmvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
                                  String name){
        //设置编码类型
        response.setContentType("text/html;charset=utf-8");
        System.out.println("接收到的paramName=" + name);
        //调用monsterService
        List<Monster> monsters = monsterService.findMonsterByName(name);
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

    //处理登录的方法，返回要请求转发/重定向的字符串
    @RequestMapping(value = "/monster/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response,
                        @RequestParam(value = "monsterName") String mName){
        System.out.println("接收到的mName=" + mName);
        request.setAttribute("mName",mName);
        boolean login = monsterService.login(mName);
        if (login){
            //登录成功
            return "/login_ok.jsp";
        }else {
            //登录失败
            return "forward:/login_error.jsp";
        }

    }
    //编写方法，返回json数据格式
    /** 
     * @author Jeffrey
     * @date 17:40 2024/5/1
     * @param request
     * @param response 
     * @return java.util.List<com.jeffrey.entity.Monster>
     *     目标方法返回的结果是返回给 springmvc 通过反射调用的位置
     *     在 springmvc 进行反射调用的地方，接收结果并解析
     *     方法上标注了 @ResponseBody 注解 ，希望以json 格式返回
     *     如果需要返回其他类型，可通过增加value 进行处理
     **/
    @RequestMapping(value = "/monster/list/json")
    @ResponseBody
    public List<Monster> listMonsterByJson(HttpServletRequest request,
                                                HttpServletResponse response){
        List<Monster> monsters = monsterService.listMonster();
        return monsters;

    }
}
