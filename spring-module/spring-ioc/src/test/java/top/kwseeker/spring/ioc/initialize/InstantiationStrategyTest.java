package top.kwseeker.spring.ioc.initialize;

import org.junit.Test;
import org.springframework.beans.factory.support.CglibSubclassingInstantiationStrategy;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.SimpleInstantiationStrategy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Constructor;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

public class InstantiationStrategyTest {

    /**
     * 测试Spring实例化策略
     */
    @Test
    public void testInstantiationStrategy() throws NoSuchMethodException {
        //bean定义
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(User.class);
        //注入属性
        rootBeanDefinition.getPropertyValues()
                .add("name","Arvin")
                .add("age",18);
        //实例化策略
        SimpleInstantiationStrategy simpleStrategy = new SimpleInstantiationStrategy();
        //DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        //User user = (User) strategy.instantiate(rootBeanDefinition, "Arvin", beanFactory);

        CglibSubclassingInstantiationStrategy cglibStrategy = new CglibSubclassingInstantiationStrategy();

        //注意这里走的是默认无参构造函数
        User user1 = (User) simpleStrategy.instantiate(rootBeanDefinition, "Arvin", null);
        //走带参数的构造函数
        User user2 = (User) simpleStrategy.instantiate(rootBeanDefinition, "Cindy", null,
                User.class.getDeclaredConstructor(String.class, int.class), "cindy", 16);

        //instantiateWithMethodInjection 是 protected 方法
        User user3 = (User) cglibStrategy.instantiate(rootBeanDefinition, "Bob", null);
    }

    @Test
    public void testSimpleInstantiationProcess() throws Exception {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(User.class);
        //注入属性
        rootBeanDefinition.getPropertyValues()
                .add("name","Arvin")
                .add("age",18);

        if (!rootBeanDefinition.hasMethodOverrides()) {
            Constructor<?> constructorToUse;
            //synchronized (rootBeanDefinition.constructorArgumentLock)
            {
                final Class<?> clazz = rootBeanDefinition.getBeanClass();
                if (clazz.isInterface()) {
                    throw new Exception("Specified class is an interface");
                }
                if (System.getSecurityManager() != null) {
                    constructorToUse = AccessController.doPrivileged((PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
                } else {
                    constructorToUse = clazz.getDeclaredConstructor();
                }
                //下次实例化直接使用此构造器方法
                //bd.resolvedConstructorOrFactoryMethod = constructorToUse;

                //BeanUtils.instantiateClass(constructorToUse);
                ReflectionUtils.makeAccessible(constructorToUse);
                Object instance = constructorToUse.newInstance();
            }
        } else {
            //instantiateWithMethodInjection(bd, beanName, owner);
            throw new Exception("Method Injection not supported in SimpleInstantiationStrategy");
        }
    }
}
