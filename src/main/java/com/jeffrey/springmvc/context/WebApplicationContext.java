package com.jeffrey.springmvc.context;

import com.jeffrey.springmvc.XmlParser;
import com.jeffrey.springmvc.annotation.Controller;
import com.jeffrey.springmvc.annotation.Service;

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

    //表示spring容器配置文件
    //得到的是：classpath:springmvc.xml 需要进行处理
    private String configLocation;
    //无参构造器
    public WebApplicationContext() {
    }

    public WebApplicationContext(String configLocation) {
        this.configLocation = configLocation;
    }


    public ConcurrentHashMap<String, Object> getSingletons() {
        return singletons;
    }

    public void init(){
        String basePackage = XmlParser.getBasePackage(configLocation.split(":")[1]);
        System.out.println("basePackage=" + basePackage);
        String[] packageNames = basePackage.split(",");
        if (packageNames.length > 0){
            for (String packageName :packageNames) {
                scanPackage(packageName);
            }
        }
        System.out.println("classFullPathList=" + classFullPathList);
        //将扫描到的类反射创建实例放入容器
        executeInstance();
        System.out.println("singletons=" + singletons);
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
                    else if (aClass.isAnnotationPresent(Service.class)) {//如果类有@Serivce注解

                        //先获取到Service的value值=> 就是注入时的beanName
                        Service serviceAnnotation =
                                aClass.getAnnotation(Service.class);

                        String beanName = serviceAnnotation.value();
                        if ("".equals(beanName)) {//说明没有指定value, 我们就使用默认的机制注入Service
                            //可以通过接口名/类名[首字母小写]来注入ioc容器
                            //1.得到所有接口的名称=>反射
                            Class<?>[] interfaces = aClass.getInterfaces();

                            Object instance = aClass.newInstance();
                            //2. 遍历接口，然后通过多个接口名来注入
                            for (Class<?> anInterface : interfaces) {
                                //接口名->首字母小写
                                String beanName2 = anInterface.getSimpleName().substring(0, 1).toLowerCase() +
                                        anInterface.getSimpleName().substring(1);
                                singletons.put(beanName2, instance);
                            }
                            //3. 留一个作业,使用类名的首字母小写来注入bean
                            //   通过 aClass 来即可.

                        } else {//如果有指定名称,就使用该名称注入即可
                            singletons.put(beanName, aClass.newInstance());
                        }
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

        }
    }
}
