package top.kwseeker.spring.config.introduction;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    /**
     * FactoryBean的使用
     * 返回使用工厂Bean getObject() 创建的实例
     */
    @Bean
    public OkHttpClient okHttpClient() {
        OkHttpClientFactory factory = new OkHttpClientFactory();
        //Class<?> clazz = factory.getObjectType();
        //if (clazz != null && clazz.isInstance(bean)) {
        //    return (OkHttpClient) clazz.cast(bean);
        //}
        return factory.getObject();
    }
}
