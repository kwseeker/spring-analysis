package top.kwseeker.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.spring.aop.aspect.LogAspect;
import top.kwseeker.spring.aop.service.GoodsInfo;
import top.kwseeker.spring.aop.service.PayService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class AspectMain {

    public static void main(String[] args) {
        log.info("application start ...");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);

        PayService payService = context.getBean(PayService.class);
        payService.createOrder();
        payService.payOrder();
        payService.deliveryGoods();

        List<GoodsInfo> goodsInfo = payService.queryGoodsInfo(10001L, new Date(), Arrays.asList(1, 2));
        log.info("测试切面对象属性: " + goodsInfo.toString());
        LogAspect aspect = (LogAspect) context.getBean("logAspect");
        aspect.setTest(true);
        log.info("测试切面对象属性: " + payService.queryGoodsInfo(10001L, new Date(), Arrays.asList(1, 2)).toString());
        aspect.setTest(false);
        log.info("测试切面对象属性: " + payService.queryGoodsInfo(10001L, new Date(), Arrays.asList(1, 2)).toString());

        context.registerShutdownHook();
    }
}
