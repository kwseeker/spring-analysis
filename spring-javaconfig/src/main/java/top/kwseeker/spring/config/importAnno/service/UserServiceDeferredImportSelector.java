package top.kwseeker.spring.config.importAnno.service;

import org.springframework.context.annotation.DeferredImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

// 如果getImportGroup返回自定义Group, 会调用自定义Group的process方法
// 如果getImportGroup返回 null, 会调用DefaultDeferredImportSelectorGroup的process方法,即调用selectImports
public class UserServiceDeferredImportSelector implements DeferredImportSelector {

    @Override
    public Class<? extends Group> getImportGroup() {
        // 这个返回值决定调用DeferredImportSelector.selectImports  如果null
        // 还是调用Group.selectImports
        return  MyGroup.class;
    }

    // getImportGroup() 返回 null, 执行这个
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{"top.kwseeker.spring.config.importAnno.service.UserService", Admin.class.getName()};
    }

    //新版Spring还多了这个接口方法
    //@Override
    //public Predicate<String> getExclusionFilter() {
    //    return null;
    //}

    // getImportGroup() 返回不为 null, 执行这里的process()
    // 分组利用归类，同一组的bean只影响本组的顺序
    private static class MyGroup implements DeferredImportSelector.Group{

        AnnotationMetadata metadata;

        @Override
        public void process(AnnotationMetadata metadata, DeferredImportSelector selector) {
            this.metadata=metadata;
        }

        @Override
        public Iterable<Entry> selectImports() {
            List<Entry> list=new ArrayList<>();
            list.add(new Entry(this.metadata,"top.kwseeker.spring.config.importAnno.service.UserService"));
            list.add(new Entry(this.metadata, "top.kwseeker.spring.config.importAnno.service.Admin"));
            return  list;
        }
    }
}
