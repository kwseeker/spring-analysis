package top.kwseeker.spring.myioc.factory;

public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);
}
