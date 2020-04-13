package top.kwseeker.spring.ioc.annotation.config;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.spring.ioc.entity.Program;
import top.kwseeker.spring.ioc.entity.User;
import top.kwseeker.spring.ioc.service.DiamondUserService;
import top.kwseeker.spring.ioc.service.VipUserService;

public class MultiConfigurationExampleTest {
    @Test
    public void testImportAnnotation() {
        //ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MultiConfigurationExample.class, ConfigurationAnnotationExample.class);
        //有了@Import就不用再像上面那样写了
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MultiConfigurationExample.class);
        VipUserService obj1 = (VipUserService) applicationContext.getBean("vipUserService");
        VipUserService obj2 = ((DiamondUserService)applicationContext.getBean("diamondUserService")).getVipUserService();
        System.out.println(obj1);
        System.out.println(obj2);

        Program program = applicationContext.getBean(Program.class);  //ImportSelector只能byType
        User user = (User) applicationContext.getBean("user");
        System.out.println(program);
        System.out.println(user);
    }
}
