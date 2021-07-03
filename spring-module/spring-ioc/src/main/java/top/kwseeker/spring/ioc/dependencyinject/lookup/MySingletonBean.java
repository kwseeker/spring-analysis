package top.kwseeker.spring.ioc.dependencyinject.lookup;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

@Component
public class MySingletonBean {

    public void showMessage(){
        MyPrototypeBean bean = getPrototypeBean();
        System.out.println("Hi, the time is "+bean.getDateTime());
    }

    @Lookup
    public MyPrototypeBean getPrototypeBean(){
        //spring will override this method
        return null;
    }
}