package top.kwseeker.spring.ioc.release;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;

@Data
public class OuterBean implements InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware, BeanClassLoaderAware {

    @Autowired
    private InnerBean innerBean;

    private String beanName;
    private ClassLoader classLoader;
    private BeanFactory beanFactory;

    OuterBean() {
        System.out.println("call OuterBean()...");
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        System.out.println("call BeanClassLoaderAware setBeanClassLoader()...");
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("call BeanFactoryAware setBeanFactory()...");
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("call BeanNameAware setBeanName()...");
        this.beanName = name;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("call DisposableBean destroy()...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("call InitializingBean afterPropertiesSet()...");
    }

    private void initMethod() {
        System.out.println("call init()...");
    }

    private void destroyMethod() {
        System.out.println("call destroyMethod()...");
    }
}
