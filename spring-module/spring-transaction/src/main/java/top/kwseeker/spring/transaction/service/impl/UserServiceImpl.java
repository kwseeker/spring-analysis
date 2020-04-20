package top.kwseeker.spring.transaction.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.kwseeker.spring.transaction.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public void createUser(String firstName, String lastName) {
        jdbcTemplate.update("insert into user (name) values(?)", firstName);
        int i = 1/0;
        jdbcTemplate.update("insert into user (name) values(?)", lastName);
    }

    public void addMoney(String id, int count) {
        jdbcTemplate.update("insert into account (accountName, money) value (?, ?)", id, count);
        //createUser里面抛异常，验证对account的操作是否会被回退
        this.createUser("xxx", "xx");
    }
}
