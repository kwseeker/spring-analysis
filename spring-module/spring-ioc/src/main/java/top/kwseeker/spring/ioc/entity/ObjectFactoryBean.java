package top.kwseeker.spring.ioc.entity;

import org.springframework.beans.factory.FactoryBean;

public class ObjectFactoryBean implements FactoryBean<Integer> {

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
