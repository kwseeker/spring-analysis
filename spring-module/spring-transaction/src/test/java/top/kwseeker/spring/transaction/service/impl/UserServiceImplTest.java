package top.kwseeker.spring.transaction.service.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.kwseeker.spring.transaction.service.UserService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations="classpath:spring-tx.xml")
public class UserServiceImplTest {

    //@Autowired
    //private UserService userService;
    //@Autowired
    //private ApplicationContext applicationContext;

    @Test
    public void createUser() {
        //这两行和类上面的注解是等效的
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-tx.xml");
        //DataSourceTransactionManager transactionManager = (DataSourceTransactionManager) context.getBean("txManager");
        UserService userService = context.getBean(UserService.class);
        //userService.createUser("Arvin", "Lee");
        userService.addMoney("555", 2000);
    }
}
