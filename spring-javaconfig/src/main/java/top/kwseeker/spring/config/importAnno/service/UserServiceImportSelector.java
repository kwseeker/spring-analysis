package top.kwseeker.spring.config.importAnno.service;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

public class UserServiceImportSelector implements ImportSelector {

    // 可以以字符串的形式注册多个Bean
    // 字符串必须是类的完整限定名  getBean不能根据名字获取获取的， 必须要根据类型获取
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{"top.kwseeker.spring.config.importAnno.service.UserService", Admin.class.getName()};
    }
}
