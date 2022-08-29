package top.kwseeker.spring.annotation.anno01;

import org.springframework.stereotype.Component;

@Component
public class BaseService {

    public void handle() {
        System.out.println("handle in BaseService!");
    }
}
