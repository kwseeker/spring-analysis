package top.kwseeker.spring.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import top.kwseeker.spring.aop.service.GoodsInfo;

import java.util.Date;
import java.util.List;

/**
 * 日志切面
 */
@Slf4j
@Component  //加入spring容器统一管理，@Aspect注解并不会被Spring扫描（所以需要@Component注释或在配置类中用@Bean方法注册）
@Aspect     //SpringAOP切面不能作为其他切面的增强,会被自动排除在自动代理之外
public class LogAspect {

    private boolean isTest = false;

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

    //1 匹配 top.kwseeker.spring.aop 包和所有子孙包中类的方法
    //@Pointcut("within(top.kwseeker.spring.aop..*)")
    //2 匹配类 top.kwseeker.spring.aop.service.WechatPayService 中所有的方法
    //@Pointcut("within(top.kwseeker.spring.aop.service.WechatPayService)")
    //3 匹配 top.kwseeker.spring.aop 包(不包括子孙包)中类的方法
    //@Pointcut("within(top.kwseeker.spring.aop.*)")
    @Pointcut("within(top.kwseeker.spring.aop.AppConfig)")      //TODO 这种写法为何@Bean方法不会被拦截？但是上一种可以？
    private void anyAopModuleMethod() {}

    @Pointcut("execution(public * top.kwseeker.spring.aop.service.PayService.payOrder(..))")
    private void onlyPayMethod() {}

    //@Pointcut("execution(public * top.kwseeker.spring.aop.service.PayService.queryGoodsInfo(long, java.util.Date, java.util.List))")
    //@Pointcut(value = "execution(public * top.kwseeker.spring..PayService.queryGoodsInfo(..)) " +
    //        "&& args(userId, date, goodsIdList)")
    //private void onlyQueryGoodsInfoMethod(long userId, Date date, List<Integer> goodsIdList) {}
    @Pointcut(value = "execution(public * top.kwseeker.spring..PayService.queryGoodsInfo(..)) "
            + "&& args(var1, var2, var3)",
            //, argNames = "var1, var2, var3"
            argNames = "var1,var2,var3")
    private void onlyQueryGoodsInfoMethod(long var1, Date var2, List<Integer> var3) {}

    /*- 通知 ------------------------------------------------------------------*/

    /**
     * 为何会拦截 SmartInitializingSingleton.afterSingletonsInstantiated()? AOP装载时机？
     * 17:50:06.387 [main] INFO top.kwseeker.spring.aop.aspect.LogAspect - before advice ... execution(void org.springframework.beans.factory.SmartInitializingSingleton.afterSingletonsInstantiated())
     */
    //@Before("anyPublicMethod()")
    //@Before("anyAopModuleMethod()")
    @Before("onlyPayMethod()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("before advice ... " + joinPoint.toString());
    }

    @AfterReturning(value = "onlyQueryGoodsInfoMethod(userId, date, goodsIdList)",
            returning = "retVal"
            //argNames = "userId,date,goodsIdList, retVal"
    )
    public void afterReturning(long userId, Date date, List<Integer> goodsIdList, Object retVal) {
        log.info("afterReturning advice ... ");
        log.info("返回值：retVal={}, 参数：userId={}, date={}, goodIdList={}", retVal, userId, date.toString(), goodsIdList.toString());
        //修改返回值内容
        if (isTest) {
            for (GoodsInfo goodsInfo : ((List<GoodsInfo>) retVal)) {
                goodsInfo.setName("MAC PRO");
            }
        }
    }

    //注释：网上有种说法，＠AfterReturning无法改变返回值，其实是说无法改变返回值的引用，但是返回值的内容是可以改变的。
    //看 AfterReturningAdviceInterceptor 源码就明白了，在结果返回之前先执行了Advice, 虽然返回的还是被增强方法返回的值，但是它的内容完全是可以被修改的。
    //@Override
    //public Object invoke(MethodInvocation mi) throws Throwable {
    //    Object retVal = mi.proceed();
    //    this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
    //    return retVal;
    //}

    public void setTest(boolean test) {
        isTest = test;
    }
}
