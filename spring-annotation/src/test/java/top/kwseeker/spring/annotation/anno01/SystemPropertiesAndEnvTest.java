package top.kwseeker.spring.annotation.anno01;

import org.junit.Test;

import java.util.Map;
import java.util.Properties;

/**
 * StandardEnvironment 主要是获取JVM系统属性和环境变量，存到PropertySources容器
 */
public class SystemPropertiesAndEnvTest {

    @Test
    public void testSystemProperties() {
        Properties properties = System.getProperties();
        System.out.println(properties.getProperty("java.vm.info"));
        System.out.println(properties.getProperty("java.version"));
        System.out.println(properties.getProperty("java.ext.dirs"));
        System.out.println(properties.getProperty("sun.boot.class.path"));
        System.out.println(properties.getProperty("file.separator"));
        System.out.println(properties.getProperty("java.class.path"));
    }

    @Test
    public void testSystemEnv() {
        Map<String, String> env = System.getenv();
        System.out.println(env.get("PATH"));
        System.out.println(env.get("PWD"));
        System.out.println(env.get("USER"));
        System.out.println(env.get("HOME"));
    }
}
