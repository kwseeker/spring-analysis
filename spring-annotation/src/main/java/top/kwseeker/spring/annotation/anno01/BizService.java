package top.kwseeker.spring.annotation.anno01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class BizService {

    @Autowired
    private BaseService baseService;
    //@Resource
    //private BaseService baseService;

    public void doBiz() {
        baseService.handle();
    }
}
