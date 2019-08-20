package xyz.suiwo.imooc.web.server;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import xyz.suiwo.imooc.web.servlet.DispatcherServlet;

public class TomcatServer {

    private Tomcat tomcat;
    private String[] args;

    public TomcatServer(String[] args) {
        this.args = args;
    }

    public void startServer() throws LifecycleException {
        tomcat = new Tomcat();
        tomcat.setPort(8080);

        Context context = new StandardContext();
        context.setPath("");
        context.addLifecycleListener(new Tomcat.FixContextListener());
        DispatcherServlet dispatcherServlet = new DispatcherServlet();

        // servlet注册到tomcat容器内并开启异步支持
        Tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet).setAsyncSupported(true);

        context.addServletMappingDecoded("/", "dispatcherServlet");

        // 注册到默认host容器
        tomcat.getHost().addChild(context);
        tomcat.start();

        Thread awaitThread = new Thread("tomcat_await_thread"){
            @Override
            public void run() {
                TomcatServer.this.tomcat.getServer().await();
            }
        };

        //设置成非守护线程
        awaitThread.setDaemon(false);
        awaitThread.start();
    }
}
