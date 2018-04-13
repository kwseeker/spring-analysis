package top.kwseeker.beanscopetest;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration 声明当前类为一个配置类，相当于一个Spring配置的xml文件。
 */
@Configuration
@ComponentScan("top.kwseeker.beanscopetest")
public class ScopeConfig {
}
