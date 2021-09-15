package top.kwseeker.spring.config.importAnno.beans;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
//@Component
@Component("person")  //@Import导入＠Component Bean 貌似不会自动给Bean命名，需要在@Component中指定bean的名称 否则按名称查找找不到
public class Person {

    /**
     * 使用 @Value设置依赖注入的属性
     * 1.除了可以写硬编码值
     * 2.还可以写${}  、#{}
     */
    //@Value("#{role.name}")
    @Value("Arvin")
    private String name;
}
