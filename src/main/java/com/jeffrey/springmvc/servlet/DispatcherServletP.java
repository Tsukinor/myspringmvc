package com.jeffrey.springmvc.servlet;

import com.jeffrey.springmvc.annotation.Controller;
import com.jeffrey.springmvc.annotation.RequestMapping;
import com.jeffrey.springmvc.context.WebApplicationContext;
import com.jeffrey.springmvc.handler.HandlerP;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    //定义属性 handlerList ，保存 HandlerP 对象
    private List<HandlerP> handlerPList =
            new ArrayList<>();

    WebApplicationContext webApplicationContext = null;

    @Override
    public void init() throws ServletException {
              webApplicationContext  = new WebApplicationContext();
        webApplicationContext.init();
        //调用 initHandlerMapping ，完成url 和控制器方法的映射
        initHandlerMapping();
        System.out.println("handlerPList初始化=" + handlerPList);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
        System.out.println("DispatcherServletP------doGet---被调用");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DispatcherServletP-----doPost---被调用");
        //调用方法完成分发请求
        executeDispatch(req,resp);
    }

    //完成url和处理器方法的映射
    private void initHandlerMapping(){
        if (webApplicationContext.getSingletons().isEmpty()){
            //判断当前的ioc容器是否为null
            return;
        }
        //遍历singleton 的bean对象，然后进行url映射
        for (Map.Entry<String,Object> entry :webApplicationContext.getSingletons().entrySet()) {
            //先取出容器中实例的Class对象
            Class<?> aClass = entry.getValue().getClass();
            if (aClass.isAnnotationPresent(Controller.class)){
                //取出所有方法
                Method[] methods = aClass.getMethods();
                for (Method method :methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)){
                        String contextPath = getServletContext().getContextPath();
                        String url = method.getAnnotation(RequestMapping.class).value();
                        String fullUrl = contextPath + url;
                        //创建HandlerP对象
                        HandlerP handlerP = new HandlerP(fullUrl, entry.getValue(), method);
                        handlerPList.add(handlerP);
                    }
                }

            }

        }

    }

    //通过request对象，返回handlerp对象
    private HandlerP getHandler(HttpServletRequest request){
        //1.获取到请求uri
        String requestURI = request.getRequestURI();
        //遍历handlerPList
        //注意：得到的uri和HandlerP中的url 工程路径问题
        for (HandlerP uri :handlerPList) {
            if (uri.getUrl().equals(requestURI)){
                return uri;
            }
        }
        return null;
    }

    //完成分发请求任务
    private void executeDispatch(HttpServletRequest request,
                                 HttpServletResponse response){

        HandlerP handler = getHandler(request);
        try {

            if (handler == null){
            //说明用户请求资源不存在
                PrintWriter writer = response.getWriter();
                writer.println("404NotFound");
            }else {
                //匹配成功后，反射调用控制器方法
                handler.getMethod().invoke(handler.getController(),request,response);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
