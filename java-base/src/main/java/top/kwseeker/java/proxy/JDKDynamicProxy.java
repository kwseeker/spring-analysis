package top.kwseeker.java.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKDynamicProxy {

    public static void main(String[] args) {
        ISomeService someService = new SomeService();
        MyInvocationHandler myInvocationHandler = new MyInvocationHandler(someService);
        ISomeService proxyClass = (ISomeService) myInvocationHandler.getProxyClass();

        proxyClass.doService();
        proxyClass.doService2();
        User user = proxyClass.getUserInfo();
        System.out.println(user.toString());
    }

    static class MyInvocationHandler implements InvocationHandler {
        //将被代理的接口对象引入
        private final ISomeService someService;

        public MyInvocationHandler(ISomeService someService) {
            this.someService = someService;
        }

        public Object getProxyClass(){
            return Proxy.newProxyInstance(this.getClass().getClassLoader(), someService.getClass().getInterfaces(),this);
        }

        /**
         * @param proxy     代理对象
         * @param method    被增强方法
         * @param args      方法入参数
         * @return Object   被代理方法执行返回的结果
         * @throws Throwable
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.printf("Before %s invoke ...%n", method.getName());

            //!!! 和CGLib重要的区别之一，JDK动态代理需要被代理对象实例完成基本功能。
            Object retVal = method.invoke(someService, args);

            System.out.printf("After %s Invoke ...%n", method.getName());
            return retVal;
        }
    }
}
