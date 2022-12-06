package top.kwseeker.spring.ioc.release;

import org.springframework.context.Lifecycle;
import org.springframework.context.Phased;
import org.springframework.stereotype.Component;

@Component
public class LifecycleBBean implements Lifecycle, Phased {

    private boolean isRunning = false;

    @Override
    public int getPhase() {
        return 10;
    }

    @Override
    public void start() {
        System.out.println("call LifeCycleBBean start()...");
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
        System.out.println("call LifeCycleBBean stop()...");
    }

    @Override
    public boolean isRunning() {
        System.out.println("call LifeCycleBBean isRunning()...");
        return isRunning;
    }
}
