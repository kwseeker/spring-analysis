package top.kwseeker.spring.myioc.factory;

public interface SingletonRegister {

    Object getSingletonInstance(String beanName);

    void addSingletonInstance(String beanName, Object bean);
}
