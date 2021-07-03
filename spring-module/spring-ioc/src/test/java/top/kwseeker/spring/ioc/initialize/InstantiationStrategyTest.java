package top.kwseeker.spring.ioc.initialize;

import net.sf.cglib.core.DebuggingClassWriter;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.*;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
        //注入属性(即使注入属性这里初始化过程也没用到)
        rootBeanDefinition.getPropertyValues()
                .add("name","Arvin")
                .add("age",18);
        //实例化策略
        SimpleInstantiationStrategy simpleStrategy = new SimpleInstantiationStrategy();
        CglibSubclassingInstantiationStrategy cglibStrategy = new CglibSubclassingInstantiationStrategy();

        //注意这里走的是默认无参构造函数
        User user1 = (User) simpleStrategy.instantiate(rootBeanDefinition, "Arvin", null);  //instantiate()中的owner参数对SimpleInstantiationStrategy是无用的
        //走带参数的构造函数
        User user2 = (User) simpleStrategy.instantiate(rootBeanDefinition, "Cindy", null,
                User.class.getDeclaredConstructor(String.class, int.class), "cindy", 19);

        //CglibSubclassingInstantiationStrategy instantiateWithMethodInjection 是 protected 方法, 无法直接调用, 是通过调用继承父类instantiate间接调用的
        User user3 = (User) cglibInstantiation(rootBeanDefinition, null, null);
        User user4 = (User) cglibInstantiation(rootBeanDefinition, null, User.class.getDeclaredConstructor(String.class, int.class), "Bob", 20);
    }

    @Test
    public void testLookUpOverride() {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/tmp/spring2");

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(User.class);
        rootBeanDefinition.getPropertyValues()
                .add("name","Arvin")
                .add("age",18);
        beanFactory.registerBeanDefinition("arvin", rootBeanDefinition);

        RootBeanDefinition managerBeanDefinition = new RootBeanDefinition(UserManager.class);
        managerBeanDefinition.getMethodOverrides()
                .addOverride(new LookupOverride("getUser", "arvin"));
        UserManager userManager = (UserManager) cglibInstantiation(managerBeanDefinition, beanFactory, null);
        User user = userManager.getUser();       //TODO 哪一步还有问题
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

    private Object cglibInstantiation(RootBeanDefinition beanDefinition, BeanFactory owner, Constructor<?> ctor, Object... args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(beanDefinition.getBeanClass());
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallbackTypes(new Class<?>[]{NoOp.class, LookupOverrideMethodInterceptor.class});  //增强方式，源码中提供了三种
        //enhancer.setCallbackFilter(method -> 0);       //对哪个方法使用哪种增强方式
        enhancer.setCallbackFilter(new MethodOverrideCallbackFilter(beanDefinition));
        Class<?> subclass = enhancer.createClass();
        Object instance = null;
        if (ctor == null) {
            instance = BeanUtils.instantiateClass(subclass);
        } else {
            try {
                Constructor<?> enhancedSubclassConstructor = subclass.getConstructor(ctor.getParameterTypes());
                instance = enhancedSubclassConstructor.newInstance(args);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    static class UserManager {
        //会被重写
        public User getUser() {
            return null;
        }
    }

    private static class MethodOverrideCallbackFilter implements CallbackFilter {

        private final RootBeanDefinition beanDefinition;

        public MethodOverrideCallbackFilter(RootBeanDefinition beanDefinition) {
            this.beanDefinition = beanDefinition;
        }

        @Override
        public int accept(Method method) {
            MethodOverride methodOverride = beanDefinition.getMethodOverrides().getOverride(method);
            if (methodOverride == null) {
                return 0;
            } else if (methodOverride instanceof LookupOverride) {
                return 1;
            }
            throw new UnsupportedOperationException("Unexpected MethodOverride subclass: " + methodOverride.getClass().getName());
        }
    }

    private static class LookupOverrideMethodInterceptor implements MethodInterceptor {

        private final RootBeanDefinition beanDefinition;
        private final BeanFactory owner;

        public LookupOverrideMethodInterceptor(RootBeanDefinition beanDefinition, BeanFactory owner) {
            this.beanDefinition = beanDefinition;
            this.owner = owner;
        }

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy mp) throws Throwable {
            System.out.println("LookupOverride intercept ...");
            //一个个地检查每一个方法是否被定义为LookupOverride, 是的话返回LookupOverride对象，不是的话返回null
            LookupOverride lo = (LookupOverride) beanDefinition.getMethodOverrides().getOverride(method);
            Assert.state(lo != null, "LookupOverride not found");
            Object[] argsToUse = (args.length > 0 ? args : null);  // if no-arg, don't insist on args at all
            if (StringUtils.hasText(lo.getBeanName())) {
                return (argsToUse != null ? this.owner.getBean(lo.getBeanName(), argsToUse) :
                        this.owner.getBean(lo.getBeanName()));
            } else {
                return (argsToUse != null ? this.owner.getBean(method.getReturnType(), argsToUse) :
                        this.owner.getBean(method.getReturnType()));
            }
        }
    }
}
