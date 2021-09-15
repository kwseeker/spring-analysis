package top.kwseeker.spring.config.importAnno;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.spring.config.importAnno.beans.Person;
import top.kwseeker.spring.config.importAnno.service.User;
import top.kwseeker.spring.config.importAnno.service.Admin;
import top.kwseeker.spring.config.importAnno.service.UserService;

public class ImportApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(GlobalConfig.class);

        //测试配置导入
        // 1 @Component
        //Person arvin = context.getBean(Person.class);
        Person arvin = (Person) context.getBean("person");
        System.out.println(arvin);
        // 2 @Configuration
        DruidDataSource bean = (DruidDataSource) context.getBean("dataSource");
        System.out.println(bean.getName());
        // 3 ImportSelector & 4 ImportBeanDefinitionRegistrar
        User admin = context.getBean(Admin.class);
        System.out.println(admin);
        UserService userService = context.getBean(UserService.class);
        System.out.println(userService.echo());

        //测试 DeferredImportSelector

    }
}
