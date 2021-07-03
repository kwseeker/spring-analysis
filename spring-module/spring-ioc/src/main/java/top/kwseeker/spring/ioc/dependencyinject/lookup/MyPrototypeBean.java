package top.kwseeker.spring.ioc.dependencyinject.lookup;

import java.time.LocalDateTime;

public class MyPrototypeBean {

    private String dateTimeString = LocalDateTime.now().toString();

    public String getDateTime() {
        return dateTimeString;
    }
}
