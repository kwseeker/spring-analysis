package top.kwseeker.spring.ioc.annotation.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.kwseeker.spring.ioc.service.UserService;
import top.kwseeker.spring.ioc.service.VipUserService;

//@Configuration
public class ConfigurationAnnotationExample {

    @Bean
    public UserService userService() {  //方法名即BeanName
        return new UserService();
    }
    //或者这样写
    //@Bean(name = "userService")
    //public UserService getUserService() {
    //    return new UserService();
    //}

    //VipUserService依赖UserService,如果不使用＠Configuration,则此处依赖会重新创建一个Bean(UserService)
    //如果配置了＠Configuration,则在处理依赖的时候会先去Bean单例池中查找依赖类型的Bean实例，从而避免创建多个实例。
    @Bean
    public VipUserService vipUserService() {
        VipUserService vipUserService = new VipUserService();
        vipUserService.setUserService(userService());
        return vipUserService;
    }
}
