package top.kwseeker.spring.ioc.annotation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import top.kwseeker.spring.ioc.service.DiamondUserService;
import top.kwseeker.spring.ioc.service.VipUserService;

@Configuration
@Import({ConfigurationAnnotationExample.class,      //依赖ConfigurationAnnotationExample配置源
        MyImportBeanDefinitionRegistrar.class,
        MyImportSelector.class})
public class MultiConfigurationExample {

    //当前配置源依赖另一个配置源的Bean,可通过＠Autowired自动注入
    @Autowired
    private VipUserService vipUserService;

    @Bean
    public DiamondUserService diamondUserService() {
        DiamondUserService diamondUserService = new DiamondUserService();
        diamondUserService.setVipUserService(vipUserService);
        return diamondUserService;
    }
}
