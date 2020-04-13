package top.kwseeker.spring.ioc.circledependency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {

    //@Autowired注入
    @Autowired
    private B b;

    //构造器注入
    //public A(B b) {
    //    this.b = b;
    //}
}
