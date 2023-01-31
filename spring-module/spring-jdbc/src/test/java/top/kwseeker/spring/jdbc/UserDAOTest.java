package top.kwseeker.spring.jdbc;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class UserDAOTest {

    @Test
    public void testCreateUser() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-beans.xml");
        UserDAO userDAO = context.getBean(UserDAO.class);
        userDAO.createUser("Arvin");
        userDAO.createUser1("Bob", 13122222222L);
        userDAO.createUser2("Cindy", 13122222223L);
        userDAO.createUser3("David", 13122222224L);
        UserDAO2 userDAO2 = context.getBean(UserDAO2.class);
        userDAO2.createUser3("Eric", 13122222225L);
    }
}