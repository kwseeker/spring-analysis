package top.kwseeker.spring.config.importAnno;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.kwseeker.spring.config.importAnno.beans.Person;
import top.kwseeker.spring.config.importAnno.service.UserServiceDeferredImportSelector;
import top.kwseeker.spring.config.importAnno.service.UserServiceImportBeanDefinitionRegistrar;

/**
 * 全局配置类，通过@Import等注解装载其他配置
 */
//本身也还是个配置类
@Configuration
//@ComponentScan(basePackages = "top.kwseeker.spring.config.importAnno")
//@ImportResource()       //可使用@ImportResource装载XML配置
//主持导入3种组件类：@Configuration, ImportSelector, ImportBeanDefinitionRegistrar, 还可以直接导入@Component注解的Bean
@Import({Person.class,                      //@Component Bean, 对比 @ComponentScan. @Import导入＠Component Bean 貌似不会自动给Bean命名，需要在@Component中指定bean的名称
        DatabaseConfig.class,               //@Configuration
        //UserServiceImportSelector.class,    //ImportSelector
        //UserServiceImportBeanDefinitionRegistrar.class      //ImportBeanDefinitionRegistrar
        UserServiceDeferredImportSelector.class             //DeferredImportSelector
})
public class GlobalConfig {

}
