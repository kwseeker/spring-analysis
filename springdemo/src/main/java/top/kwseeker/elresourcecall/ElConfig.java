package top.kwseeker.elresourcecall;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;


/**
 * @Configuration 声明当前类为一个配置类，相当于一个Spring配置的xml文件。
 */
@Configuration
@ComponentScan("top.kwseeker.elresourcecall")   //扫描目录并加载bean
@PropertySource("classpath:test.properties")    //读取属性文件, classpath 默认为 $MODULE_DIR$/src/main/resources
public class ElConfig {

    @Value("I Love you!")   //@Value设置normal的默认值
    private String normal;

    @Value("#{systemProperties['os.name']}")    //注入操作系统属性
    private String osName;

    @Value("#{ T(java.lang.Math).random() * 100.0}")    //注入表达式结果
    private double randomNumber;

    @Value("#{demoElService.another}")  //注入其他Bean属性
    private String fromAnother;

    @Value("http://www.baidu.com")  //注入URL
    private Resource testUrl;

    @Value("classpath:test.txt")    //注入文件资源
    private Resource testFile;

    @Value("${book.name}")  //属性文件已经读取可以直接使用
    private String bookName;

    @Autowired  //注入Bean实例
    private Environment environment;

    @Bean   //获取Bean实例
    public static PropertySourcesPlaceholderConfigurer propertyConfigure() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public void outputResource() {
        try {
            System.out.println(normal);
            System.out.println(osName);
            System.out.println(randomNumber);
            System.out.println(fromAnother);
            System.out.println(IOUtils.toString(testFile.getInputStream()));
            System.out.println(IOUtils.toString(testUrl.getInputStream()));
            System.out.println(bookName);
            System.out.println(environment.getProperty("book.author"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
