package top.kwseeker.spring.ioc.close;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class EBean {

    @PostConstruct
    private void initMethod() {
        System.out.println("call EBean init()...");
    }

    @PreDestroy
    private void destroyMethod() {
        System.out.println("call EBean destroyMethod()...");
    }
}
