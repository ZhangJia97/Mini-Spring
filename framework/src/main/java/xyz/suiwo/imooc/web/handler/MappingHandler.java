package xyz.suiwo.imooc.web.handler;

import xyz.suiwo.imooc.beans.BeanFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class MappingHandler {

    // 需要处理的uri
    private String uri;

    // 所对应的方法
    private Method method;

    // 所对应的方法
    private Class<?> controller;

    // 所需要的参数
    private String[] args;

    public MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }

    // 若与MappingHandler匹配成功，执行方法
    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        String requestUri = ((HttpServletRequest)req).getRequestURI();
        if(!uri.equals(requestUri)){
            return false;
        }
        Object[] parameters = new Object[args.length];
        for(int i = 0; i < args.length; i++){
            parameters[i] = req.getParameter(args[i]);
        }
        Object ctl = BeanFactory.getBean(controller);
        Object response = method.invoke(ctl, parameters);
        res.getWriter().println(response.toString());
        return true;
    }
}
