package top.kwseeker.spring.myioc.beandefinition;

import top.kwseeker.spring.myioc.config.Resource;

/**
 * 将XML配置信息读取到BeanDefinition的容器(Map)中
 */
public interface BeanDefinitionReader {
    void loadBeanDefinitions(Resource resource);
}
