package com.jeffrey.springmvc.context;

import com.jeffrey.springmvc.XmlParser;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: myspringmvc
 * @author: Jeffrey
 * @create: 2024-04-25 17:11
 * @description:
 **/
public class WebApplicationContext {

    //定义属性 classFullPathList，保存扫描包/子包的类的全路径
    private List<String> classFullPathList =
            new ArrayList<>();

    public void init(){
        String basePackage = XmlParser.getBasePackage("springmvc.xml");
        System.out.println("basePackage=" + basePackage);
        String[] packageNames = basePackage.split(",");
        if (packageNames.length > 0){
            for (String packageName :packageNames) {
                scanPackage(packageName);
            }
        }
        System.out.println(classFullPathList);

    }

    //创建方法，完成对包的扫描
    public void scanPackage(String packageName){
        //得到包所在的工作路径
        URL resource =
                this.getClass().getClassLoader()
                        .getResource("/"+ packageName.replaceAll("\\.","/"));
        System.out.println("url=" + resource);
//        String path = resource + packageName
        File dir = new File(resource.getFile());
        for (File file1 :dir.listFiles()) {
            if (file1.isDirectory()){
                scanPackage(packageName + "."+file1.getName());
            }else {
                //扫描到的文件可能是class，目前先把文件的全路径放入classFullPathList中
                String fullPath = packageName + "." + file1.getName().replaceAll(".class","");
                classFullPathList.add(fullPath);
            }
//            String path = file1.getPath();
//            System.out.println(path);
        }


    }
}
