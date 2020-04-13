package top.kwseeker.spring.myioc.factory;

public abstract class AbstractBeanFactory extends DefaultSingletonRegister implements BeanFactory {

    @Override
    public Object getBean(String beanName) {
        // 从单例的缓存中获取bean实例
        Object bean = getSingletonInstance(beanName);

        if (bean != null) {
            return bean;
        }
        // 创建bean对象，然后放入map
        bean = doCreateBean(beanName);

        // 将创建出来的bean实例，放入单例bean的缓存中
        addSingletonInstance(beanName, bean);
        return bean;
    }

    public abstract Object doCreateBean(String beanName);
}
