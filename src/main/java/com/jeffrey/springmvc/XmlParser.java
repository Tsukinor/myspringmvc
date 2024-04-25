package com.jeffrey.springmvc;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-24 20:25
 * @description:
 * 用于解析xml
 **/
public class XmlParser {

    public static String getBasePackage(String xmlFile) throws Exception {
        SAXReader saxReader = new SAXReader();
        ClassLoader classLoader = XmlParser.class.getClassLoader();
        InputStream resourceAsStream = classLoader.getResourceAsStream(xmlFile);

        Document read = saxReader.read(resourceAsStream);
        Element rootElement = read.getRootElement();
        Element element = rootElement.element("component-scan");

        Attribute attribute = element.attribute("base-package");
        String basePackage = attribute.getText();

        return basePackage;
    }
}
