package top.kwseeker.spring.ioc.dependencyinject.lookup;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(basePackageClasses = MySingletonBean.class)
public class AppConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MyPrototypeBean prototypeBean() {
        return new MyPrototypeBean();
    }

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        MySingletonBean bean = context.getBean(MySingletonBean.class);
        bean.showMessage();

        Thread.sleep(1000);

        bean = context.getBean(MySingletonBean.class);
        bean.showMessage();
    }
}
