package top.kwseeker.spring.myioc.factory;

import java.util.HashMap;
import java.util.Map;

public class DefaultSingletonRegister implements SingletonRegister {

    private Map<String, Object> sinletonObjects = new HashMap<>();

    @Override
    public Object getSingletonInstance(String beanName) {
        return this.sinletonObjects.get(beanName);
    }

    @Override
    public void addSingletonInstance(String beanName, Object bean) {

    }
}
