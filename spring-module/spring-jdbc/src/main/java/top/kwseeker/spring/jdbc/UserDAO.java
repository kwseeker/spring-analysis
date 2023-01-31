package top.kwseeker.spring.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import top.kwseeker.spring.jdbc.domain.User;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserDAO {

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void createUser(String name) {
        KeyHolder keyHolder = new GeneratedKeyHolder(); //用于存储自动生成的主键
        int ret = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement("insert into user (name) values(?)", new String[]{"name"});
            ps.setString(1, name);
            return ps;
        }, keyHolder);
        if (ret == 1) {
            Number pk = keyHolder.getKey();
            if (pk != null) {
                System.out.println(pk.toString());
                User user = jdbcTemplate.queryForObject(
                        "select * from user where id = ?",
                        (resultSet, rowNum) -> {        //定义一个RowMapper函数实例，用于将结果集转成对象实例
                            User newUser = new User();
                            newUser.setId(resultSet.getInt("id"));
                            newUser.setName(resultSet.getString("name"));
                            return newUser;
                        },
                        pk.intValue());
                if (user != null) {
                    System.out.println(user.toString());
                }
            }
        }
    }

    //使用NamedParameterJdbcTemplate通过具名参数传参
    //方式1
    public void createUser1(String name, Long phone) {
        MapSqlParameterSource sps = new MapSqlParameterSource();
        sps.addValue("phone", phone);
        sps.addValue("name", name);
        namedParameterJdbcTemplate.update("insert into user (name, phone) values(:name, :phone)", sps);
    }
    //方式2
    public void createUser2(String name, Long phone) {
        Map<String, Object> sps = new HashMap<String, Object>();
        sps.put("phone", phone);
        sps.put("name", name);
        namedParameterJdbcTemplate.update("insert into user (name, phone) values(:name, :phone)", sps);
    }
    //方式3
    public void createUser3(String name, Long phone) {
        User user = new User();
        user.setPhone(phone);
        user.setName(name);

        SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
        namedParameterJdbcTemplate.update("insert into user (name, phone) values(:name, :phone)", sps);
    }

    public User queryUser(Integer userId) {
        User user = jdbcTemplate.queryForObject(
                "select * from user where id = ?",
                (resultSet, rowNum) -> {
                    User user1 = new User();
                    user1.setId(resultSet.getInt("id"));
                    user1.setName(resultSet.getString("name"));
                    return user1;
                },
                userId);
        if (user != null) {
            System.out.println(user.toString());
        }
        return user;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
}
