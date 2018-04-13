package top.kwseeker.beanscopetest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("prototype")     //每次调用新建一个Bean的实例
public class DemoPrototypeService {
}
