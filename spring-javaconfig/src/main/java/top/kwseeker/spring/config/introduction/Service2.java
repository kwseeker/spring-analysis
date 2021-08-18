package top.kwseeker.spring.config.introduction;

import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@Service
public abstract class Service2 {

    //测试＠Lookup, 注入原型Bean
    @Lookup
    public abstract DailyStrategy dailyStrategy();

    public void doSomething() {
        System.out.println("Today's dailyStrategy: " + dailyStrategy());
    }
}
