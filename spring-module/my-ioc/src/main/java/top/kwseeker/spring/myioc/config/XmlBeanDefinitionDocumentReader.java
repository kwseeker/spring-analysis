package top.kwseeker.spring.myioc.config;

import org.dom4j.Element;
import top.kwseeker.spring.myioc.beandefinition.*;
import top.kwseeker.spring.myioc.util.ReflectUtil;

import java.util.List;

/**
 * Xml配置读取器，将xml中配置的BeanDefinition读取到BeanDefinition的注册中心
 */
public class XmlBeanDefinitionDocumentReader {

    private BeanDefinitionRegistry beanDefinitionRegistry;

    public XmlBeanDefinitionDocumentReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }


    public void loadBeanDefinitions(Element rootElement) {
        List<Element> elements = rootElement.elements();
        for (Element element : elements) {
            String name = element.getName();
            if(name.equals("bean")) {
                parseDefaultElement(element);
            } else {
                parseCustomElement(element);
            }
        }
    }

    private void parseDefaultElement(Element beanElement) {
        try {
            if(beanElement == null)
                return;

            String id = beanElement.attributeValue("id");
            String name = beanElement.attributeValue("name");
            String clazzName = beanElement.attributeValue("class");
            if(clazzName == null || "".equals(clazzName)) {
                return;
            }
            Class<?> classType = Class.forName(clazzName);
            String initMethod = beanElement.attributeValue("init-method");
            String beanName = id == null? name:id;
            beanName = beanName == null? classType.getSimpleName() : beanName;

            BeanDefinition beanDefinition = new BeanDefinition(clazzName, beanName);
            beanDefinition.setInitMethod(initMethod);
            List<Element> propertyElements = beanElement.elements();
            for (Element element : propertyElements) {
                parsePropertyElement(beanDefinition, element);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void parseCustomElement(Element element) {
    }

    //解析Bean定义中属性值到BeanDefinition中
    private void parsePropertyElement(BeanDefinition beanDefinition, Element propertyElement) {
        if(propertyElement == null)
            return;

        // 获取name属性
        String name = propertyElement.attributeValue("name");
        // 获取value属性
        String value = propertyElement.attributeValue("value");
        // 获取ref属性
        String ref = propertyElement.attributeValue("ref");

        // 如果value和ref都有值，则返回
        if (value != null && !value.equals("") && ref != null && !ref.equals("")) {
            return;
        }

        //PropertyValue就封装着一个property标签的信息
        PropertyValue pv = null;

        if (value != null && !value.equals("")) {
            // 因为spring配置文件中的value是String类型，而对象中的属性值是各种各样的，所以需要存储类型
            TypedStringValue typeStringValue = new TypedStringValue(value);

            Class<?> targetType = ReflectUtil.getTypeByFieldName(beanDefinition.getClazzName(), name);
            typeStringValue.setTargetType(targetType);

            pv = new PropertyValue(name, typeStringValue);
            beanDefinition.addPropertyValue(pv);
        } else if (ref != null && !ref.equals("")) {
            RuntimeBeanReference reference = new RuntimeBeanReference(ref);
            pv = new PropertyValue(name, reference);
            beanDefinition.addPropertyValue(pv);
        } else {
            return;
        }
    }


}
