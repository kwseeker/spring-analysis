package top.kwseeker.spring.utils.property;

import top.kwseeker.spring.utils.resource.DefaultResourceLoader;
import top.kwseeker.spring.utils.resource.Resource;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    public static Properties loadProperties(String location) throws IOException {
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        Properties properties = new Properties();
        properties.load(resource.getInputStream());
        return properties;
    }
}
