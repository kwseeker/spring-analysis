package top.kwseeker.spring.ioc;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import top.kwseeker.spring.ioc.entity.ObjectFactoryBean;
import top.kwseeker.spring.ioc.entity.User;

public class IocTest {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        User user = (User)context.getBean("bob");
        User user1 = (User)context.getBean("frank");

        Object obj = context.getBean("objectFactoryBean");
        Object obj1 = context.getBean("&objectFactoryBean");
        System.out.println(obj instanceof Integer);
        System.out.println(obj1 instanceof ObjectFactoryBean);
    }
}
