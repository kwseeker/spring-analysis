package top.kwseeker.spring.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.lang.NonNull;
import top.kwseeker.spring.jdbc.domain.User;

/**
 * JdbcDaoSupport 主要是简化了 JdbcTemplate 的注入代码
 */
public class UserDAO2 extends NamedParameterJdbcDaoSupport {

    public void createUser3(String name, Long phone) {
        User user = new User();
        user.setPhone(phone);
        user.setName(name);

        SqlParameterSource sps = new BeanPropertySqlParameterSource(user);
        getNamedParameterJdbcTemplate()
                .update("insert into user (name, phone) values(:name, :phone)", sps);
    }

    @NonNull    //这注解在程序运行的过程中不会起任何作用，只是用于IDE、编译器等自动检查
    @Override
    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        //NamedParameterJdbcDaoSupport getNamedParameterJdbcTemplate() 返回值是可能为空的
        NamedParameterJdbcTemplate namedParameterJdbcTemplate = super.getNamedParameterJdbcTemplate();
        if (namedParameterJdbcTemplate == null) {
            //SQLExceptionTranslator translator = super.getExceptionTranslator();
            //translator.translate()
            throw new NullPointerException("NamedParameterJdbcTemplate cannot be null");
        }
        return namedParameterJdbcTemplate;
    }
}
