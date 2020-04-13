package top.kwseeker.spring.ioc.service;

import lombok.Data;

@Data
public class DiamondUserService {

    private VipUserService vipUserService;
}
