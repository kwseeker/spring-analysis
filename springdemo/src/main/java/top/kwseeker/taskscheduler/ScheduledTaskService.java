package top.kwseeker.taskscheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * 计划任务服务
 */
@Service
public class ScheduledTaskService {
	
	  private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	  @Scheduled(fixedRate = 5000) //每隔5s执行一次
	  public void reportCurrentTime() {
	       System.out.println("每个5s执行一次 " + dateFormat.format(new Date()));
	   }

	  @Scheduled(cron = "0 5 12 ? * *"  ) //指定的时间运行（每天中午12:03:00）
	  public void fixTimeExecution(){
	      System.out.println("在指定的时间" + dateFormat.format(new Date())+"执行");
	  }

}
