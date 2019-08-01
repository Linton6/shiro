package com.imooc.test;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.jdbc.JdbcRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;
import com.alibaba.druid.*;

/**
 * @Author Linton
 * @Date 2019/7/31 16:59
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */

public class JdbcRealmTest {
    // 创建一个数据源
    DruidDataSource dataSource = new DruidDataSource();
    {
        dataSource.setUrl("jdbc:mysql://localhost:3306/company");
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
    }
    @Test
    public void testAuthentication(){
        JdbcRealm jdbcRealm = new JdbcRealm();
        //设置jdbcrelam的数据源
        jdbcRealm.setDataSource(dataSource);
        jdbcRealm.setPermissionsLookupEnabled(true); // 权限默认是false,不会去查询权限数据。记住啦

        // 认证自定义的数据库表
        String sql = "select password from test_user where user_name = ?";
        jdbcRealm.setAuthenticationQuery(sql);
        String roleSql = "select role_name from test_user_role where user_name = ? ";
        jdbcRealm.setUserRolesQuery(roleSql);


        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(jdbcRealm);

        // 2.主体就是subject提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("xiaoming", "654321");

        subject.login(token); // 底层通过securityManager的login方法

        System.out.println("isAuthenticated： "+ subject.isAuthenticated());

        subject.checkRoles("user");

        /*subject.checkRole("admin");

        subject.checkRoles("admin","user");

        subject.checkPermission("user:select");*/

    }
}

