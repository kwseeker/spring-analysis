package top.kwseeker.spring.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 日志切面
 */
@Slf4j
@Component  //加入spring容器统一管理，@Aspect注解并不会被Spring扫描（所以需要@Component注释或在配置类中用@Bean方法注册）
@Aspect     //SpringAOP切面不能作为其他切面的增强,会被自动排除在自动代理之外
public class LogAspect {

    /*- 切点 ------------------------------------------------------------------*/
    /**
     * SpringAOP切点标识符
     * execution        匹配方法执行连接点
     * within           限制匹配某些类型中的连接点
     * this
     * target
     * args
     * @target
     * @args
     * @within
     * @annotation
     * AspectJ AOP还包含其他标识符。
     */

    //匹配全部public方法
    @Pointcut("execution(public * *(..))")      //切点表达式
    private void anyPublicMethod() {}           //切点签名

    //@Pointcut("within(top.kwseeker.spring.aop..*)")
    //@Pointcut("within(top.kwseeker.spring.aop.*)")
    @Pointcut("within(top.kwseeker.spring.aop.AppConfig.*)")
    private void anyAopModuleMethod() {}

    /*- 通知 ------------------------------------------------------------------*/

    /**
     * 为何会拦截 SmartInitializingSingleton.afterSingletonsInstantiated()? AOP装载时机？
     * 17:50:06.387 [main] INFO top.kwseeker.spring.aop.aspect.LogAspect - before advice ... execution(void org.springframework.beans.factory.SmartInitializingSingleton.afterSingletonsInstantiated())
     */
    //@Before("anyPublicMethod()")
    @Before("anyAopModuleMethod()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("before advice ... " + joinPoint.toString());
    }

}
