package xyz.suiwo.imooc.web.servlet;

import xyz.suiwo.imooc.web.handler.HandlerManager;
import xyz.suiwo.imooc.web.handler.MappingHandler;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet implements Servlet {
    @Override
    public void init(ServletConfig config) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        for(MappingHandler mappingHandler : HandlerManager.mappingHandlerList){
            try {
                if(mappingHandler.handle(req, res)){
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
