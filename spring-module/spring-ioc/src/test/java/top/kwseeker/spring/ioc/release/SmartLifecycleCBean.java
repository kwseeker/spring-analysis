package top.kwseeker.spring.ioc.release;

import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class SmartLifecycleCBean implements SmartLifecycle {

    private boolean isRunning = false;

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        System.out.println("call SmartLifecycleCBean stop(callback)...");
        callback.run();
        //回调默认是
        //() -> {
        //    latch.countDown();
        //    countDownBeanNames.remove(beanName);
        //    if (logger.isDebugEnabled()) {
        //        logger.debug("Bean '" + beanName + "' completed its stop procedure");
        //    }
        //}
    }

    @Override
    public void start() {
        System.out.println("call SmartLifecycleCBean start()...");
        isRunning = true;
    }

    @Override
    public void stop() {
        System.out.println("call SmartLifecycleCBean stop()...");
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        System.out.println("call SmartLifecycleCBean isRunning()...");
        return isRunning;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
