package com.jeffrey.test;

import com.jeffrey.springmvc.XmlParser;
import org.junit.jupiter.api.Test;

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

}
