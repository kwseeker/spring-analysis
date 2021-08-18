package top.kwseeker.spring.config.introduction;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 测试单例Bean(Service)依赖原型Bean(DailyStrategy)
 * 假设服务每天都有不同的处理策略。
 */
@Component
@Scope("prototype")
public class DailyStrategy {

    public void handle() {
        System.out.println(this.toString());
    }
}
