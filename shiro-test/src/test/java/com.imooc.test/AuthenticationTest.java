package com.imooc.test;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author Linton
 * @Date 2019/7/31 15:59
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */

public class AuthenticationTest {
    SimpleAccountRealm simpleAccountRealm = new SimpleAccountRealm(); // SimpleAccountRealm不支持添加权限

    @Before
    public void addUser(){
        simpleAccountRealm.addAccount("Linton", "123456", "admin","user");
    }

    @Test
    public void testAuthentication(){
        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(simpleAccountRealm);

        // 2.主体就是subject提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("Linton", "123456");

        subject.login(token); // 底层通过securityManager的login方法

        System.out.println("isAuthenticated： "+ subject.isAuthenticated());

        subject.checkRoles("admin","user");




    }
}

