package main.java.top.kwseeker.helloworld;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        /* helloworld.iml 指定了配置文件的路径
        <configuration>
            <fileset id="fileset" name="Spring Config" removed="false">
              <file>file://$MODULE_DIR$/src/main/resources/spring-config.xml</file>
              <file>file://$MODULE_DIR$/src/spring-config.xml</file>
            </fileset>
        </configuration>
        */
        //创建一个Spring的IOC容器对象
        ApplicationContext context = new ClassPathXmlApplicationContext("main/resources/spring-config.xml");
        //从IOC容器获取Bean实例
        HelloWorld helloWorld = (HelloWorld) context.getBean("helloWorld");
        helloWorld.sayHello();
    }
}
