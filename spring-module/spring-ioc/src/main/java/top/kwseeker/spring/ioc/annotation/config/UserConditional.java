package top.kwseeker.spring.ioc.annotation.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class UserConditional implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        if(conditionContext.getBeanFactory().containsBean("user")) {
            return true;
        }
        System.out.println("bean创建条件不满足，bean user还未创建");
        return false;
    }
}
