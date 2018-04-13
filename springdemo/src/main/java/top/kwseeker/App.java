package top.kwseeker;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.beanscopetest.DemoPrototypeService;
import top.kwseeker.beanscopetest.DemoSingletonService;
import top.kwseeker.beanscopetest.ScopeConfig;
import top.kwseeker.elresourcecall.ElConfig;
import top.kwseeker.elresourcecall.ResourceConfig;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        /*=Bean的作用域================================================*/
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ElConfig.class);
        ElConfig resourceService = context.getBean(ElConfig.class);
        resourceService.outputResource();
        context.close();

        /*=Bean的作用域================================================*/
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ScopeConfig.class);
//
//        DemoSingletonService demoSingletonService1 = context.getBean(DemoSingletonService.class);
//        DemoSingletonService demoSingletonService2 = context.getBean(DemoSingletonService.class);
//
//        DemoPrototypeService demoPrototypeService1 = context.getBean(DemoPrototypeService.class);
//        DemoPrototypeService demoPrototypeService2 = context.getBean(DemoPrototypeService.class);
//
//        System.out.println("Singleton类Bean，两次获取的结果是否相同：" + demoSingletonService1.equals(demoSingletonService2));
//        System.out.println("Prototype类Bean，两次获取的结果是否相同：" + demoPrototypeService1.equals(demoPrototypeService2));
//
//        context.close();
    }
}
