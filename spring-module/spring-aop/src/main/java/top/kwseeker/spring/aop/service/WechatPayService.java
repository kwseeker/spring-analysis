package top.kwseeker.spring.aop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class WechatPayService implements PayService {

    @Override
    public void createOrder() {
        log.info("create order ...");
    }

    @Override
    public void payOrder() {
        log.info("pay order ...");
    }

    @Override
    public void deliveryGoods() {
        log.info("delivery order goods ...");
    }
}
