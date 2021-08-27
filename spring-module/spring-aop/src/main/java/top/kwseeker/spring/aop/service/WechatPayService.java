package top.kwseeker.spring.aop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    public List<GoodsInfo> queryGoodsInfo(long userId, Date date, List<Integer> goodsIdList) {
        System.out.println("query goods info ...");
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.setName("MAC AIR");
        return Collections.singletonList(goodsInfo);
    }
}
