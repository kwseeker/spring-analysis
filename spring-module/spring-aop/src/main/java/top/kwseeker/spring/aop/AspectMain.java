package top.kwseeker.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.spring.aop.service.PayService;

@Slf4j
public class AspectMain {

    public static void main(String[] args) {
        log.info("application start ...");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        PayService payService = context.getBean(PayService.class);
        payService.createOrder();
        payService.payOrder();
        payService.deliveryGoods();

        context.registerShutdownHook();
    }
}
