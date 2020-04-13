package top.kwseeker.spring.myioc.beandefinition;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean在XML和实例对象之间的中间态
 */
@Data
@RequiredArgsConstructor
public class BeanDefinition {

    @NonNull private String clazzName;
    @NonNull private String beanName;
    private String initMethod;
    //定义Bean的属性
    private List<PropertyValue> propertyValues = new ArrayList<>();

    public void addPropertyValue(PropertyValue propertyValue) {
        this.propertyValues.add(propertyValue);
    }
}
