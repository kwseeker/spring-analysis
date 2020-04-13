package top.kwseeker.spring.ioc.annotation.config;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class ObjectFactoryBean2 implements FactoryBean<Integer> {

    @Override
    public Integer getObject() throws Exception {
        return new Integer("1");
    }

    @Override
    public Class<?> getObjectType() {
        return Integer.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
