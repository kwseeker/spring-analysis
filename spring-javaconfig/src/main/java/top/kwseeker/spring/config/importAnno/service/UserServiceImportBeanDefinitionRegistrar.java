package top.kwseeker.spring.config.importAnno.service;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 这个使用起来显得有点麻烦
 */
public class UserServiceImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
        GenericBeanDefinition adminBd = new GenericBeanDefinition();
        adminBd.setBeanClass(Admin.class);
        GenericBeanDefinition userServiceBd = new GenericBeanDefinition();
        userServiceBd.setBeanClass(UserService.class);
        beanDefinitionRegistry.registerBeanDefinition("admin", adminBd);
        beanDefinitionRegistry.registerBeanDefinition("userService", userServiceBd);
    }
}
