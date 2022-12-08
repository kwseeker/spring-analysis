package top.kwseeker.spring.ioc.close;

import org.springframework.stereotype.Component;

@Component
public class FBean {

    public void destruct() {
        System.out.println("call FBean destruct() ...");
    }
}
