package top.kwseeker.spring.ioc.aware;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/application-aware.xml"})
public class UserWithAwareTest {

    @Test
    public void getBeanName() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application-aware.xml");
        UserWithAware user = (UserWithAware) applicationContext.getBean("arvin");
        System.out.println(user.getBeanName());
        /**
         * beanName = "arvin"
         * beanFactory = {DefaultListableBeanFactory@2191} "org.springframework.beans.factory.support.DefaultListableBeanFactory@56928307: defining beans [arvin]; root of factory hierarchy"
         * applicationContext = {ClassPathXmlApplicationContext@2168} "org.springframework.context.support.ClassPathXmlApplicationContext@416c58f5: startup date [Wed Apr 08 00:01:03 CST 2020]; root of context hierarchy"
         */
    }
}
