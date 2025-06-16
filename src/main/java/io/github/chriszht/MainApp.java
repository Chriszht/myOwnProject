package io.github.chriszht;

import javax.servlet.ServletException;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import java.io.File;

public class MainApp {

    public static void main(String[] args) throws ServletException, LifecycleException {
        // 1. 创建嵌入式Tomcat服务器
        Tomcat tomcat = new Tomcat();

        // 2. 设置端口
        tomcat.setPort(8080);

        // 3. 设置工作目录
        tomcat.setBaseDir("temp");

        // 4. 添加Web应用
        String contextPath = "/WeatherApp";
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        Context context = tomcat.addWebapp(contextPath, webappDir);

        // 5. 配置Servlet
        Tomcat.addServlet(context, "WeatherServlet", new io.github.chriszht.controller.WeatherServlet());
        context.addServletMappingDecoded("/weather", "WeatherServlet");

        // 6. 启动服务器
        tomcat.start();
        System.out.println("Server started on port " + tomcat.getConnector().getPort());
        tomcat.getServer().await();
    }
}