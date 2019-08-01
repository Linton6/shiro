package com.imooc.dao.impl;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * @Author Linton
 * @Date 2019/8/1 8:14
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */
@Component
public class UserDaoImpl implements UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public User getUserByUserName(String userName) {
        String sql = "select username,password from users where username = ?";
        List<User> list = jdbcTemplate.query(sql, new String[]{userName},new RowMapper<User>() { // 创建RowMapper匿名对象
            @Override
            public User mapRow(ResultSet resultSet, int i) throws SQLException {
                // 将查询到的结果集放置在对象中
                User user = new User();
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        });

        if (CollectionUtils.isEmpty(list)) return null;
        return list.get(0);
    }

    @Override
    public List<String> queryRolesByUserName(String userName) {
        String sql = " select role_name from user_roles where username = ?";
        return jdbcTemplate.query(sql, new String[]{userName}, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet resultSet, int i) throws SQLException {
                return resultSet.getString("role_name");
            }
        });
    }
}

