package com.imooc.test;

import com.imooc.shiro.realm.CustomRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.Test;

/**
 * @Author Linton
 * @Date 2019/7/31 18:33
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */

public class CustomRealmTest {


    @Test
    public void testAuthentication(){

        CustomRealm customRealm = new CustomRealm();

        // 1.构建SecurityManager环境
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        defaultSecurityManager.setRealm(customRealm);

        // *******加密处理是在Realm中  注意啦  *******
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        matcher.setHashAlgorithmName("md5");// 设置加密名称
        matcher.setHashIterations(1);// 设置加密次数
        customRealm.setCredentialsMatcher(matcher); // 设置加密对象

        // 2.主体就是subject提交认证请求
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("Linton", "1234567");

        subject.login(token); // 底层通过securityManager的login方法

        System.out.println("isAuthenticated： "+ subject.isAuthenticated());

        subject.checkRoles("admin","user");
        subject.checkPermissions("user:delete","user:add");

//        subject.checkRole("admin");
//
//        subject.checkPermission("user:update");

    }
}

