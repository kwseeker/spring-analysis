package top.kwseeker.taskscheduler;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("top.kwseeker.taskscheduler")    //自动加载计划任务并在应用启动时执行
@EnableScheduling //1
public class TaskSchedulerConfig {

}
