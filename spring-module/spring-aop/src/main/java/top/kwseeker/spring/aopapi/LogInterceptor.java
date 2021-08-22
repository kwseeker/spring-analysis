package top.kwseeker.spring.aopapi;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.util.Arrays;
import java.util.List;

public class LogInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        String methodName = ((ReflectiveMethodInvocation) invocation).getMethod().getName();
        List<Object> args = Arrays.asList(invocation.getArguments());
        long startTime = System.currentTimeMillis();
        System.out.println("执行目标方法【" + methodName +"】前, 入参" + args + ", startTime=" + startTime);
        Object ret=invocation.proceed();
        System.out.println("执行目标方法【" + methodName +"】后, costTime=" + (System.currentTimeMillis() - startTime) + "ms");

        return ret;
    }
}
