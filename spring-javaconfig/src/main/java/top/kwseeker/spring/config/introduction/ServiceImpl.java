package top.kwseeker.spring.config.introduction;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class ServiceImpl implements Service, InitializingBean, DisposableBean {

    @Autowired
    private IEncryptor encryptor;

    private Repository repository;

    public ServiceImpl(Repository repository) {
        this.repository = repository;
    }

    // 1
    @Override
    public void customInit () {
        System.out.println(getClass().getName() + " customInit() ...");
    }
    // 2
    @PostConstruct
    public void postConstructMethod() {
        System.out.println(getClass().getName() + " postConstructMethod() ...");
    }
    // 3
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println(getClass().getName() + " afterPropertiesSet() ...");
    }

    // 1
    @Override
    public void customDestroy () {
        System.out.println(getClass().getName() + " customDestroy() ...");
    }
    // 2
    @PreDestroy             //对应XML的destroy-method
    public void preDestroyMethod() {
        System.out.println(getClass().getName() + " preDestroyMethod() ...");
    }
    // 3
    @Override
    public void destroy() throws Exception {
        System.out.println(getClass().getName() + " destroy() ...");
    }

    public void doSomething() {
        System.out.println("do something ...");
        System.out.println(repository.toString());
        System.out.println(encryptor.toString());
    }
}
