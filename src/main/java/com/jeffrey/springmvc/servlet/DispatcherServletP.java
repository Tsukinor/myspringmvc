package com.jeffrey.springmvc.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeffrey.springmvc.annotation.Controller;
import com.jeffrey.springmvc.annotation.RequestMapping;
import com.jeffrey.springmvc.annotation.RequestParam;
import com.jeffrey.springmvc.annotation.ResponseBody;
import com.jeffrey.springmvc.context.WebApplicationContext;
import com.jeffrey.springmvc.handler.HandlerP;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
    public void init(ServletConfig servletConfig) throws ServletException {

        String configLocation =
                servletConfig.getInitParameter("contextConfigLocation");

        webApplicationContext  = new WebApplicationContext(configLocation);
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
//                        String contextPath = getServletContext().getContextPath();
                        String url = method.getAnnotation(RequestMapping.class).value();
//                        String fullUrl = contextPath + url;
                        //创建HandlerP对象
                        HandlerP handlerP = new HandlerP(url, entry.getValue(), method);
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
                //1.得到目标方法的形参参数信息[对应的是数组]
                Class<?>[] parameterTypes = handler.getMethod().getParameterTypes();
                //2.创建一个参数数组--》对应的是实参数组，在后面反射调用目标方法时使用
                Object[] params = new Object[parameterTypes.length];
                //3.遍历形参数组，根据形参数组信息，将实参填充到实参数组中
                for (int i = 0; i < parameterTypes.length; i++) {
                    //取出每一个形参类型
                    Class<?> parameterType = parameterTypes[i];
                    //如果这个形参是HttpServletRequest，将request 填充到 params
                    //在原生springmvc中是按照类型来匹配，这里简化通过名字来匹配
                    if ("HttpServletRequest".equals(parameterType.getSimpleName())){
                        params[i] = request;
                    }else if ("HttpServletResponse".equals(parameterType.getSimpleName())){
                        params[i] = response;
                    }
                }
                //将http请求参数封装到 params 数组中，注意填充实参时候的顺序问题
                //1. 获取http请求的参数集合
                //http://localhost:8080/monster/find?name=牛魔王&hobby=打篮球&hobby=喝酒
                //2. 返回的Map<String,String[]> String:表示http请求的参数名
                //   String[]:表示http请求的参数值,为什么是数组
                //      hobby=打篮球&hobby=喝酒
                //处理提交的数据中文乱码
                request.setCharacterEncoding("utf-8");//没能处理........老老实实用英文吧
                Map<String, String[]> parameterMap = request.getParameterMap();
                //2.进行遍历 parameterMap 将请求参数 填充到 params 实参数组
                for(Map.Entry<String,String[]> entry : parameterMap.entrySet()){
                    //取出key ， 这name 对应的就是 请求的参数名
                    String name = entry.getKey();
                    //这里只考虑提交的参数是单值的情况，即不考虑类似 checkbox 提交的数据
                    String value = entry.getValue()[0];
                    //我们得到请求的参数对应的目标方法的第几个形参，然后将其填充
                    //编写一个方法，得到请求的参数对应的是第几个形参
                    int indexRequestParameterIndex =
                            getIndexRequestParameterIndex(handler.getMethod(), name);
                    if (indexRequestParameterIndex != -1){
                        //说明找到对应的位置
                        params[indexRequestParameterIndex] = value;
                    }else {
                        //说明并没有找到 @RequestParam 注解对应的参数，就会使用默认机制进行匹配
                        //1.得到目标方法的所有形参的名称---专门编写一个方法获取形参名
                        //2.对得到目标方法的所有形参进行遍历
                        //如果匹配就把当前请求的参数值，填充到 params
                        List<String> parameterNames = getParameterNames(handler.getMethod());
                        for (int i = 0;i < parameterNames.size();i++){
                            //如果请求参数名和目标方法的形参名一样，说明匹配成功
                            if (name.equals(parameterNames.get(i))){
                                params[i] = value;//填充到数组
                                break;
                            }
                        }

                    }

                }

                  //1. 下面这样写法，其实是针对目标方法是 m(HttpServletRequest request , HttpServletResponse response)
                  //2. 这里准备将需要传递给目标方法的 实参=>封装到参数数组=》然后以反射调用的方式传递给目标方法
                  //3. public Object invoke(Object obj, Object... args)..
                Object result = handler.getMethod().invoke(handler.getController(), params);
                //这里就是对返回的结果进行解析=》原生 springmvc 是通过视图解析器来完成
                //这里简化直接解析
                if (result instanceof String){
                    String viewName = (String)result;
                    if (viewName.contains(":")){
                     //说明你返回的String 结果forward:/login_ok.jsp 或者 redirect:/xxx/xx/xx.xx
                        String viewType = viewName.split(":")[0];
                        String viewPage = viewName.split(":")[1];
                        //判断是forward 还是 redirect
                        if (viewType.equals("forward")){
                            //进行请求转发
                            request.getRequestDispatcher(viewPage).forward(request,response);
                        }else if (viewType.equals("redirect")){
                            response.sendRedirect(viewPage);
                        }
                    }else {
                        //没有 ： 默认是请求转发
                        request.getRequestDispatcher(viewName).forward(request,response);
                    }
                } else if (result instanceof ArrayList){
                    //判断目标方法是否有 @ResponseBody 注解
                    Method method = handler.getMethod();
                    if (method.isAnnotationPresent(ResponseBody.class)){
                        //把result 【ArrayList】 转成 json 格式返回
                        //需要使用到Java 如何将 ArrayList 转换为 json
                        //这里使用 jackson 包下的工具类
                        ObjectMapper objectMapper = new ObjectMapper();
                        String resultJson = objectMapper.writeValueAsString(result);
                        //简单处理，直接返回
                        response.setContentType("text/html;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(resultJson);
                        writer.flush();
                        writer.close();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //编写方法，返回请求参数是目标方法的第几个形参
    /**
     * @author Jeffrey
     * @date 14:50 2024/5/1
     * @param method 目标方法
     * @param name 请求参数名
     * @return int 是目标方法的第几个参数
     **/
    public int getIndexRequestParameterIndex(Method method,String name){
        //1.得到method的所有的形参参数
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //取出当前的形参参数
            Parameter parameter = parameters[i];
            //判断 parameter 是不是有 @RequestParam 注解
            if (parameter.isAnnotationPresent(RequestParam.class)){
                //取出当前这个参数的 RequestParam 的value
                RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                String value = annotation.value();
                //这里就是匹配的比较
                if (name.equals(value)){
                    return i;
                }
            }
        }
        return -1;
    }

    //编写方法，得到目标方法所有的形参名称，并放入到集合中返回
    /** 
     * @author Jeffrey
     * @date 15:52 2024/5/1 
     * @param method 目标方法
     * @return java.util.List<java.lang.String> 所有形参的名称，并放入到集合中返回
     **/
    public List<String> getParameterNames(Method method){
        List<String> parametersList = new ArrayList<>();
        //获取到所有参数的名称-----一个小细节！！
        //在默认情况下 parameter.getName() 得到的名字不是真正的名字
        //而是 arg0,arg1,arg2....  z在这里引入一个插件，使用Java8特性
        Parameter[] parameters = method.getParameters();
        //遍历 parameters 取出名称 放入 parametersList
        for (Parameter parameter :parameters) {
            parametersList.add(parameter.getName());
        }
        System.out.println("目标方法的形参列表parametersList===" + parametersList);

        return parametersList;
    }
}
