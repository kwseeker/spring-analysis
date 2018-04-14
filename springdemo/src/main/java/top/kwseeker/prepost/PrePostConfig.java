package top.kwseeker.prepost;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import top.kwseeker.prepost.BeanWayService;
import top.kwseeker.prepost.JSR250WayService;

@Configuration
@ComponentScan("top.kwseeker.prepost")
public class PrePostConfig {
	
	@Bean(initMethod="init",destroyMethod="destroy") //1
	BeanWayService beanWayService(){
		return new BeanWayService();
	}
	
	@Bean
	JSR250WayService jsr250WayService(){
		return new JSR250WayService();
	}

}
