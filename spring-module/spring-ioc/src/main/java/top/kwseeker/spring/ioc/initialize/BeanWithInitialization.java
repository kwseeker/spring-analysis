package top.kwseeker.spring.ioc.initialize;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class BeanWithInitialization implements InitializingBean {

    public BeanWithInitialization() {
        System.out.println("创建Bean");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("这里实现Bean初始化操作");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("Bean创建后执行");
    }

    @PreDestroy
    public void preDestory() {
        System.out.println("Bean销毁前，释放资源");
    }
}
