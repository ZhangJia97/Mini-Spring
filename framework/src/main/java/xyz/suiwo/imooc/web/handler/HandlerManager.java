package xyz.suiwo.imooc.web.handler;

import xyz.suiwo.imooc.web.mvc.Controller;
import xyz.suiwo.imooc.web.mvc.RequestMapping;
import xyz.suiwo.imooc.web.mvc.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class HandlerManager {
    public static List<MappingHandler> mappingHandlerList = new ArrayList<>();

    // 把Controller类挑选出来，并将类中的带有@RequestMapping方法初始化成MappingHandler
    public static void resolveMappingHandler(List<Class<?>> classList){
        for(Class<?> cls : classList){
            if(cls.isAnnotationPresent(Controller.class)){
                parseHandlerFromController(cls);
            }
        }
    }

    // 解析controller类
    private static void parseHandlerFromController(Class<?> cls) {
        Method[] methods = cls.getDeclaredMethods();
        for(Method method : methods){
            if(!method.isAnnotationPresent(RequestMapping.class)){
                continue;
            }
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            List<String> paramNameList = new ArrayList<>();
            for(Parameter parameter : method.getParameters()){
                if(parameter.isAnnotationPresent(RequestParam.class)){
                    paramNameList.add(parameter.getDeclaredAnnotation(RequestParam.class).value());
                }
            }
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params);
            HandlerManager.mappingHandlerList.add(mappingHandler);
        }
    }
}
