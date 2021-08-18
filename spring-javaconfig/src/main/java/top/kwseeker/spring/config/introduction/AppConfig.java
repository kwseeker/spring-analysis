package top.kwseeker.spring.config.introduction;

import org.springframework.context.annotation.*;

@Configuration
@ComponentScan  //这个注解为了扫描@Service等
public class AppConfig {

    @Bean(initMethod = "customInit", destroyMethod = "customDestroy")
    //对应XML的init-method destroy-method, 具体注解解析流程：CommonAnnotationBeanPostProcessor
    public Service service() {
        //构造器注入
        return new ServiceImpl(repository());
    }

    //TODO 实现原理源码分析
    @Bean("encryptor")
    //@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)     //encryptor = {Encryptor$$EnhancerBySpringCGLIB$$2b750655@1864} "top.kwseeker.spring.config.introduction.Encryptor@a8ef162"
    //@Scope(value = "prototype", proxyMode = ScopedProxyMode.INTERFACES)         //encryptor = {$Proxy15@1846} "top.kwseeker.spring.config.introduction.Encryptor@6f36c2f0"
    @Scope(value = "prototype", proxyMode = ScopedProxyMode.NO)
    //encryptor = {$Proxy15@1846} "top.kwseeker.spring.config.introduction.Encryptor@6f36c2f0"
    //@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public IEncryptor encryptor() {
        return new Encryptor();
    }

    @Bean
    public Repository repository() {
        return new JdbcRepository();
    }

    @Bean
    @Primary    //使用＠Primary指定使用多个同类型Bean中的哪一个，即使有多个同类型Bean通过byType获取实例也不会报错
    public Service3 primaryService3() {
        return new Service3("primary");
    }
    @Bean
    public Service3 backupService3() {
        return new Service3("backup");
    }
}
