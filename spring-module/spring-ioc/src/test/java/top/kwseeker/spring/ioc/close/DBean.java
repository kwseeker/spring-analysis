package top.kwseeker.spring.ioc.close;

public class DBean {

    private void initMethod() {
        System.out.println("call DBean init()...");
    }

    private void destroyMethod() {
        System.out.println("call DBean destroyMethod()...");
    }
}
