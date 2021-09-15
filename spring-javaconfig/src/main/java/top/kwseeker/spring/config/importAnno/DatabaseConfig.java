package top.kwseeker.spring.config.importAnno;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 数据库配置类
 */
//@Configuration    //被@Import导入后就不需要这个注解了
@PropertySource("classpath:db.properties")      // 使用@PropertySource 引入外部属性资源文件
public class DatabaseConfig {

    //<bean class="com.alibaba.druid.pool.DruidDataSource" id="dataSource">
    //    <property name="username" value="${mysql.username}"></property>
    //    <property name="password" value="${mysql.password}"></property>
    //    <property name="url"  value="${mysql.url}"></property>
    //    <property name="driverClassName" value="${mysql.driverClassName}"></property>
    //</bean>
    @Value("${mysql.username}")
    private String name;
    @Value("${mysql.password}}")
    private String password;
    @Value("${mysql.url}")
    private String url;
    @Value("${mysql.driverClassName}")
    private String driverClassName;

    @Bean(name = {"dataSource","ds"})
    public DruidDataSource dataSource(){
        DruidDataSource dataSource=new DruidDataSource();
        dataSource.setName(name);
        dataSource.setPassword(password);
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driverClassName);
        return  dataSource;
    }
}
