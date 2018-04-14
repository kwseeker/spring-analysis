package top.kwseeker.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class DemoListener implements ApplicationListener<DemoEvent> {

	public void onApplicationEvent(DemoEvent event) {
		
		String msg = event.getMsg();
		System.out.println("我DemoListener接收到了DemoPublisher发布的消息：" + msg);

	}

}
