package top.kwseeker.spring.utils.resource;

/**
 * 资源加载器
 * 从资源文件等读取内容到BeanDefinition
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";

    Resource getResource(String location);
}
