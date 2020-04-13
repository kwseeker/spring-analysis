package top.kwseeker.spring.myioc.beandefinition;

/**
 * BeanDefinition注册中心，实现类就是前面说的存储BeanDefinition的Map容器
 */
public interface BeanDefinitionRegistry {

    /**
     * 注册BeanDefinition
     * @param beanName
     * @param beanDefinition
     */
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    /**
     * 通过beanName从注册中心获取BeanDefinition
     * @return
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 获取所有beanName
     * @return
     */
    String[] getBeanDefinitionNames();

    /**
     * 判断注册中心是否包含beanName的BeanDefinition
     * @param beanName
     * @return
     */
    boolean containBeanDefinition(String beanName);
}
