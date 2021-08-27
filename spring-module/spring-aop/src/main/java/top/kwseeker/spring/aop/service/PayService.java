package top.kwseeker.spring.aop.service;

import java.util.Date;
import java.util.List;

public interface PayService {

    void createOrder();

    void payOrder();

    void deliveryGoods();

    List<GoodsInfo> queryGoodsInfo(long userId, Date date, List<Integer> goodsIdList);
}
