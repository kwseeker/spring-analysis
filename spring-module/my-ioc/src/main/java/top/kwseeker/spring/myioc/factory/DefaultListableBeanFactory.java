package top.kwseeker.spring.myioc.factory;

import top.kwseeker.spring.myioc.beandefinition.*;
import top.kwseeker.spring.myioc.util.ReflectUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultListableBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {

    private Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return null;
    }

    @Override
    public boolean containBeanDefinition(String beanName) {
        return false;
    }

    @Override
    public Object doCreateBean(String beanName) {
        // 根据bean获取BeanDefinition信息
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            return null;
        }
        // 将要创建的bean的信息通过配置文件（xml）来配置
        // 读取配置文件中的bean信息，然后去创建bean对象
        // 查找beanDefinitionMap集合，获取知道beanname的BeanDefinition对象
        // 通过BeanDefinition对象的bean的信息去创建bean实例
        // 1.实例化---获取class信息，然后通过反射去创建该对象
        Object beanInstance = createInstance(beanDefinition);
        // 2.设置属性--获取该bean的PropertyValue集合，去遍历该集合，获取PropertyValue对象中的name和value
        // 需要对value进行处理（需要将字符串值的value，转成指定类型的value）
        // 通过反射去设置value值。
        setProperty(beanInstance, beanDefinition);
        // 3.初始化---获取bean标签的init-method属性，然后去通过反射，调用实例化对象的指定方法
        initMethod(beanInstance, beanDefinition);
        return beanInstance;
    }

    private void initMethod(Object beanInstance, BeanDefinition beanDefinition) {
        // 完成aware接口的处理
        if (beanInstance instanceof Aware) {
            if (beanInstance instanceof BeanFactoryAware) {
                ((BeanFactoryAware) beanInstance).setBeanFactory(this);
            }
        }
        // 完成InitializingBean的接口处理
        // 完成bean标签中init-method属性的处理
        String initMethod = beanDefinition.getInitMethod();
        if (initMethod == null) {
            return;
        }
        ReflectUtil.invokeMethod(beanInstance, initMethod);
    }

    /**
     * 依赖注入
     *
     * @param beanInstance
     * @param beanDefinition
     */
    private void setProperty(Object beanInstance, BeanDefinition beanDefinition) {
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues == null || propertyValues.size() == 0) {
            return;
        }
        for (PropertyValue propertyValue : propertyValues) {
            handlePropertyValue(beanInstance, propertyValue);
        }
    }

    private void handlePropertyValue(Object beanInstance, PropertyValue propertyValue) {
        // 获取属性name名称
        String name = propertyValue.getName();
        // 此value值需要进行处理
        Object value = propertyValue.getValue();

        Object valueToUse = null;
        if (value instanceof TypedStringValue) {
            TypedStringValue typedStringValue = (TypedStringValue) value;
            // 此stringValue就是property标签的value属性值，它是String类型，但是我们需要不同的类型
            String stringValue = typedStringValue.getValue();

            // 最终要将stringValue转换之后的类型，也就是bean实例中指定属性的类型
            Class<?> targetType = typedStringValue.getTargetType();

            // 使用策略模式进行优化
            if (targetType == Integer.class) {
                valueToUse = Integer.parseInt(stringValue);
            } else if (targetType == String.class) {
                valueToUse = stringValue;
            }
            // ....

        } else if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference runtimeBeanReference = (RuntimeBeanReference) value;
            String beanName = runtimeBeanReference.getRef();
            // 进行递归调用，去创建属性对应的bean实例
            valueToUse = getBean(beanName);
        }

        // 完成bean的属性填充
        ReflectUtil.setProperty(beanInstance, name, valueToUse);
    }

    private Object createInstance(BeanDefinition beanDefinition) {
        String beanClassName = beanDefinition.getClazzName();
        return ReflectUtil.createObject(beanClassName);
    }
}
