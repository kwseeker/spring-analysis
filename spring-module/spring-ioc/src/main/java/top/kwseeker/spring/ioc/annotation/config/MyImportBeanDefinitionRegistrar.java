package top.kwseeker.spring.ioc.annotation.config;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;
import top.kwseeker.spring.ioc.entity.User;

public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        //创建BeanDefinition
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(User.class.getName());
        // 注册到容器
        registry.registerBeanDefinition("user",rootBeanDefinition);
    }
}
