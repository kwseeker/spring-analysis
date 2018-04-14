package top.kwseeker;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.annotation.DemoConfig;
import top.kwseeker.annotation.DemoService;
import top.kwseeker.aware.AwareConfig;
import top.kwseeker.aware.AwareService;
import top.kwseeker.beanscopetest.DemoPrototypeService;
import top.kwseeker.beanscopetest.DemoSingletonService;
import top.kwseeker.beanscopetest.ScopeConfig;
import top.kwseeker.conditional.ConditionConifg;
import top.kwseeker.conditional.ListService;
import top.kwseeker.elresourcecall.ElConfig;
import top.kwseeker.elresourcecall.ResourceConfig;
import top.kwseeker.event.DemoPublisher;
import top.kwseeker.event.EventConfig;
import top.kwseeker.prepost.BeanWayService;
import top.kwseeker.prepost.JSR250WayService;
import top.kwseeker.prepost.PrePostConfig;
import top.kwseeker.profile.DemoBean;
import top.kwseeker.profile.ProfileConfig;
import top.kwseeker.taskexecutor.AsyncTaskService;
import top.kwseeker.taskexecutor.TaskExecutorConfig;
import top.kwseeker.taskscheduler.TaskSchedulerConfig;

/**
 * Hello world!
 *
 */
public class App
{
    public static void main( String[] args )
    {
        /*=Bean初始化和销毁==================================*/
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PrePostConfig.class);

        BeanWayService beanWayService = context.getBean(BeanWayService.class);
        JSR250WayService jsr250WayService = context.getBean(JSR250WayService.class);

        context.close();

        /*=profile=========================================*/
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext();
//
//        context.getEnvironment().setActiveProfiles("dev"); //1
//        context.register(ProfileConfig.class);//2
//        context.refresh(); //3
//
//        DemoBean demoBean = context.getBean(DemoBean.class);
//        System.out.println(demoBean.getContent());
//
//        context.close();

        /*=事件============================================*/
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(EventConfig.class);
//
//        DemoPublisher demoPublisher = context.getBean(DemoPublisher.class);
//        demoPublisher.publish("hello application event");
//
//        context.close();

        /*=组合注解=========================================*/
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(DemoConfig.class);
//
//        DemoService demoService =  context.getBean(DemoService.class);
//        demoService.outputResult();
//
//        context.close();

        /*=条件注解====================================================*/
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(ConditionConifg.class);
//
//        ListService listService = context.getBean(ListService.class);
//
//        System.out.println(context.getEnvironment().getProperty("os.name")
//                + "系统下的列表命令为： "
//                + listService.showListCmd());
//
//        context.close();

        /*=计划任务====================================================*/
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(TaskSchedulerConfig.class);

        /*=Aware获取Spring服务=========================================*/
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(TaskExecutorConfig.class);
//
//        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);    // 获取异步任务服务
//
//        for(int i =0 ;i<10;i++){
//            asyncTaskService.executeAsyncTask(i);       //执行异步任务
//            asyncTaskService.executeAsyncTaskPlus(i);   //执行异步任务
//        }
//        context.close();

        /*=Aware获取Spring服务=========================================*/
//        AnnotationConfigApplicationContext context =
//                new AnnotationConfigApplicationContext(AwareConfig.class);
//
//        AwareService awareService = context.getBean(AwareService.class);
//        awareService.outputResult();
//
//        context.close();

        /*=Spring EL与资源调用==========================================*/
//        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ElConfig.class);
//        ElConfig resourceService = context.getBean(ElConfig.class);
//        resourceService.outputResource();
//        context.close();

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
