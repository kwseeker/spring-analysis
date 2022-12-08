package top.kwseeker.spring.ioc.close;

import org.springframework.stereotype.Component;

@Component
public class InnerBean {

    public InnerBean() {
        System.out.println("InnerBean created");
    }
}
