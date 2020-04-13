package top.kwseeker.spring.ioc.lazyinit;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Lazy
//@Scope(scopeName = "prototype")
public class LazyPrototypeBean {

    public LazyPrototypeBean() {
        System.out.println("bean实例化");
    }
}
