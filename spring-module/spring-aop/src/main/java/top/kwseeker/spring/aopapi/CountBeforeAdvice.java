package top.kwseeker.spring.aopapi;

import org.springframework.aop.MethodBeforeAdvice;

import java.lang.reflect.Method;

public class CountBeforeAdvice implements MethodBeforeAdvice {

    private int count = 0;

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        count++;
        System.out.println(method.getName() + "执行次数：" + count);
    }

    public int getCount() {
        return count;
    }
}
