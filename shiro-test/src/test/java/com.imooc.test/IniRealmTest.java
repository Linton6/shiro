package com.imooc.test;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author Linton
 * @Date 2019/7/31 16:40
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description t通过IniRealm 来实现shiro的认证，授权过程
 */

public class IniRealmTest {

    @Test
    public void testAuthentication(){

        IniRealm iniRealm = new IniRealm("classpath:user.ini");

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(iniRealm);

        // 2.主体就是subject提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("Linton", "123456");

        subject.login(token); // 底层通过securityManager的login方法

        System.out.println("isAuthenticated： "+ subject.isAuthenticated());

        subject.checkRole("admin");

        subject.checkPermission("user:update");

    }
}

