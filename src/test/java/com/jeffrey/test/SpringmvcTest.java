package com.jeffrey.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffrey.entity.Monster;
import com.jeffrey.springmvc.XmlParser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-24 20:34
 * @description:
 **/
public class SpringmvcTest {

    @Test
    public void readXML(){

        try {
            String basePackage = XmlParser.getBasePackage("springmvc.xml");
            System.out.println(basePackage);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void TestJackson(){
        List<Monster> monsters =
                new ArrayList<>();
        monsters.add(new Monster(100, "牛魔王", "芭蕉扇", 400));
        monsters.add(new Monster(200, "老猫妖怪", "抓老鼠", 200));

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String s = objectMapper.writeValueAsString(monsters);
            System.out.println(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
