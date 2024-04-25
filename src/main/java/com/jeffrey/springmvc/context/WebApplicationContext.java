package com.jeffrey.springmvc.context;

import com.jeffrey.springmvc.XmlParser;
import com.jeffrey.springmvc.annotation.Controller;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

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

    //定义一个单例池，在初始化时，将实例注入
    private ConcurrentHashMap<String,Object> singletons =
            new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, Object> getSingletons() {
        return singletons;
    }

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
        //将扫描到的类反射创建实例放入容器
        executeInstance();
        System.out.println(singletons);
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

    //将扫描到的类，在满足情况的条件下，注入到 singletons 中
    public void executeInstance(){
        if (classFullPathList.size() > 0){
            for (String classPath :classFullPathList) {
                try {
                    Class<?> aClass = Class.forName(classPath);
                    if (aClass.isAnnotationPresent(Controller.class)){
                        Object instance = aClass.newInstance();
                        String simpleName = aClass.getSimpleName().substring(0,1).toLowerCase()
                                + aClass.getSimpleName().substring(1);
                        singletons.put(simpleName,instance);
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
