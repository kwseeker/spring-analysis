package top.kwseeker.spring.ioc.initialize;

import org.junit.Test;
import org.springframework.beans.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.support.SimpleInstantiationStrategy;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Bean实例化后属性填充流程
 */
public class BeanPropertyPopulateTest {

    /**
     * 测试Bean实例化后的属性填充流程
     */
    @Test
    public void testBeanInitialization() {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(User.class);
        rootBeanDefinition.getPropertyValues()
                .add("name","Arvin")
                .add("age", 18);
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        beanFactory.registerBeanDefinition("arvin", rootBeanDefinition);

        //断点加到AbstractAutowireCapableBeanFactory$populateBean(...)

        System.out.println(beanFactory.getBean("arvin").toString());
    }

    /**
     * 模拟实现spring在Bean实例化后填充属性的流程
     */
    @Test
    public void testBeanInitializationDetail() throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, NoSuchFieldException {
        RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(User.class);
        rootBeanDefinition.getPropertyValues()
                .add("name","Arvin")
                .add("age",18);
        SimpleInstantiationStrategy simpleStrategy = new SimpleInstantiationStrategy();
        //实例化
        //这里传递owner只是对于lookup-method replaced-method这两种依赖注入有用（增强逻辑也是bean中定义的），这里没有用到直接传null
        User beanInstance = (User) simpleStrategy.instantiate(rootBeanDefinition, "arvin", null);
        System.out.println(beanInstance.toString());

        //填充属性
        BeanWrapper bw = new BeanWrapperImpl(beanInstance);
        bw.setConversionService(null);                                  //暂时不管现在是空的
        ((PropertyEditorRegistrySupport)bw).useConfigValueEditors();    //暂时不管现在是空的
        //源码还执行了applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);   //先不管，现在没用到
        applyPropertyValues("arvin", rootBeanDefinition, bw, rootBeanDefinition.getPropertyValues());

        System.out.println(bw.getWrappedInstance().toString());
    }

    /**
     * 为Bean实例注入属性
     * @param beanName  bean名称
     * @param mbd       根BeanDefinition
     * @param bw        初始化后的Bean半成品
     * @param pvs       属性和值的数组集合
     */
    protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs)
            throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        //省略非空判断、系统安全策略设置、属性值转换器处理
        List<PropertyValue> original = ((MutablePropertyValues)pvs).getPropertyValueList();
        List<PropertyValue> deepCopy = new ArrayList<>(original.size());
        for (PropertyValue pv : original) {
            //对每个属性进行值转换处理，具体做什么？TODO
            String propertyName = pv.getName();
            Object originalValue = pv.getValue();
            //Object resolvedValue = valueResolver.resolveValueIfNecessary(pv, originalValue);
            //Object convertedValue = resolvedValue;
            //boolean convertible = bw.isWritableProperty(propertyName) &&
            //        !PropertyAccessorUtils.isNestedOrIndexedProperty(propertyName);
            //if (convertible) {
            //    convertedValue = convertForProperty(resolvedValue, propertyName, bw, converter);
            //}
            //测试案例中实际处理结果：
            Object resolvedValue = originalValue;
            Object convertedValue = originalValue;
            boolean convertible = true;
            // Possibly store converted value in merged bean definition,
            // in order to avoid re-conversion for every created bean instance.
            if (resolvedValue == originalValue) {
                if (convertible) {
                    pv.setConvertedValue(convertedValue);
                }
                deepCopy.add(pv);
            }
        }
        //bw.setPropertyValue(new MutablePropertyValues(deepCopy));
        for (PropertyValue pv : new MutablePropertyValues(deepCopy).getPropertyValueList()) {
            Class<?> wrappedClass = bw.getWrappedClass();
            Class<?> propertyType = wrappedClass.getDeclaredField(pv.getName()).getType();
            Method writeMethod = wrappedClass.getDeclaredMethod("set" + pv.getName().substring(0,1).toUpperCase() + pv.getName().substring(1), propertyType);
            ReflectionUtils.makeAccessible(writeMethod);
            writeMethod.invoke(bw.getWrappedInstance(), pv.getValue());
        }
    }
}
