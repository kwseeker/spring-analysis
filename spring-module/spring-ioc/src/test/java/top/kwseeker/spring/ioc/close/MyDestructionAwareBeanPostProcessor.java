package top.kwseeker.spring.ioc.close;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class MyDestructionAwareBeanPostProcessor implements DestructionAwareBeanPostProcessor {

    @Override
    public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
        System.out.println("call MyDestructionAwareBeanPostProcessor postProcessBeforeDestruction()...");
        if (bean instanceof FBean) {
            ((FBean) bean).destruct();
        }
    }

    @Override
    public boolean requiresDestruction(Object bean) {
        return bean instanceof FBean;
    }
}
