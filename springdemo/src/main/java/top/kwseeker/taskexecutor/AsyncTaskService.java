package top.kwseeker.taskexecutor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 任务执行类
 */
@Service
@Async  //此类内所有方法都是异步方法
public class AsyncTaskService {
	
//	@Async //声明异步方法
    public void executeAsyncTask(Integer i){
        System.out.println("ִ执行异步任务: "+i);
    }

    @Async
    public void executeAsyncTaskPlus(Integer i){
        System.out.println("执行异步任务+1： "+(i+1));
    }

}
