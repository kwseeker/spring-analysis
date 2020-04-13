package top.kwseeker.spring.ioc.annotation.config;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.spring.ioc.service.DiamondUserService;
import top.kwseeker.spring.ioc.service.UserService;
import top.kwseeker.spring.ioc.service.VipUserService;

public class ConfigurationAnnotationTest {

    @Test
    public void testConfigurationAnnotation() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(ConfigurationAnnotationExample.class);
        UserService obj1 = (UserService) applicationContext.getBean("userService");
        UserService obj2 = ((VipUserService)applicationContext.getBean("vipUserService")).getUserService();
        System.out.println(obj1);
        System.out.println(obj2);
    }
    /**
     * 有@Configuration
     * top.kwseeker.spring.ioc.service.UserService@40ef3420
     * top.kwseeker.spring.ioc.service.UserService@40ef3420
     *
     * 没有＠Configuration注解
     * top.kwseeker.spring.ioc.service.UserService@3bd40a57
     * top.kwseeker.spring.ioc.service.UserService@fdefd3f
     */
}
