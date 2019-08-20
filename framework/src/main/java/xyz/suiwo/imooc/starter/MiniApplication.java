package xyz.suiwo.imooc.starter;

import org.apache.catalina.LifecycleException;
import xyz.suiwo.imooc.beans.BeanFactory;
import xyz.suiwo.imooc.core.ClassScanner;
import xyz.suiwo.imooc.web.handler.HandlerManager;
import xyz.suiwo.imooc.web.server.TomcatServer;

import java.util.List;

public class MiniApplication {
    public static void run(Class<?> cls, String[] args){
        System.out.println("Hello Mini-Spring!");
        // 创建一个Tomcat服务
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            // 启动tomcat
            tomcatServer.startServer();

            // 扫描项目中当前cls目录下的所有包
            List<Class<?>> classList = ClassScanner.scannerClass(cls.getPackage().getName());

            // 初始化所有bean
            BeanFactory.init(classList);

            // 初始化所有的MappingHandler
            HandlerManager.resolveMappingHandler(classList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
