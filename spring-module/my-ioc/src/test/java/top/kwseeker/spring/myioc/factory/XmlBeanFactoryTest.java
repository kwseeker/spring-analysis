package top.kwseeker.spring.myioc.factory;

import org.junit.Test;
import top.kwseeker.spring.myioc.config.ClasspathResource;
import top.kwseeker.spring.myioc.config.Resource;
import top.kwseeker.spring.po.User;

public class XmlBeanFactoryTest {

    @Test
    public void testBeanFactory() {
        String location = "beans.xml";
        Resource resource = new ClasspathResource(location);
        BeanFactory beanFactory = new XmlBeanFactory(resource);
        User user = (User)beanFactory.getBean("arvin");
        System.out.println(user);
    }
}
