package top.kwseeker.spring.ioc.annotation.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import top.kwseeker.spring.ioc.entity.Program;

public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{Program.class.getName()};// 只能根据byType
    }
}