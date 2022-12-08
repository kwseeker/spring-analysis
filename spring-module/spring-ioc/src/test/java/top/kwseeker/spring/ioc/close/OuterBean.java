package top.kwseeker.spring.ioc.close;

import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PreDestroy;

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
        System.out.println("call OuterBean BeanClassLoaderAware setBeanClassLoader()...");
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("call OuterBean BeanFactoryAware setBeanFactory()...");
        this.beanFactory = beanFactory;
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("call OuterBean BeanNameAware setBeanName()...");
        this.beanName = name;
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("call OuterBean DisposableBean destroy()...");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("call OuterBean preDestroy()...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("call OuterBean InitializingBean afterPropertiesSet()...");
    }

    private void initMethod() {
        System.out.println("call OuterBean init()...");
    }

    private void destroyMethod() {
        System.out.println("call OuterBean destroyMethod()...");
    }
}
