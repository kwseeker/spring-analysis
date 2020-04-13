package top.kwseeker.spring.myioc.beandefinition;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PropertyValue {

    private String name;
    private Object value;
}
