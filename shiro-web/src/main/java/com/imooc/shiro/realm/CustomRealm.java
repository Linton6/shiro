package com.imooc.shiro.realm;

import com.imooc.dao.UserDao;
import com.imooc.vo.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Author Linton
 * @Date 2019/7/31 18:25
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */

public class CustomRealm extends AuthorizingRealm {
    Map<String, String> userMap = new HashMap<String, String>(16);

    @Resource
    private UserDao userDao; // 把UserDao注入进来，就可以，使用userDao而不需要新建new操作！！！利用了IOC的概念         源自spring.xml配置的第10行，<context:component-scan base-package="com.imooc"/> <!-- 扫描路径 -->

    // 授权（权限）
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        // 从数据库或缓存来通过用户获取角色数据
        Set<String> roles = getRolesByUserName(userName);
        // 从数据库或缓存来通过用户获取权限数据
        Set<String> permission = getPermissionByUserName(userName);

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        simpleAuthorizationInfo.setStringPermissions(permission);

        return simpleAuthorizationInfo;
    }

    /**
     * 模拟数据库获取权限数据
     * @param userName
     * @return
     */
    private Set<String> getPermissionByUserName(String userName) {
        Set<String> sets = new HashSet<String>();
        sets.add("user:delete");
        sets.add("user:add");
        return sets;
    }

    /**
     * 模拟数据库获取角色数据
     * @param userName
     * @return
     */
    private Set<String> getRolesByUserName(String userName) {
        System.out.println("从数据库中获取授权数据");
        List<String> list = userDao.queryRolesByUserName(userName);
        Set<String> sets = new HashSet<String>(list);

        return sets;

    }

    // 认证（登录）
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 1.从主体穿过来的认证信息中，获得用户名
        String userName = (String) authenticationToken.getPrincipal();

        // 2.通过用户名到数据库中获取凭证（密码）
        String password = getPasswordByUserName(userName);
        if (password == null) {
            return null;
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, password, "customRealm");
        authenticationInfo.setCredentialsSalt(ByteSource.Util.bytes(userName));
        return authenticationInfo;
    }

    /**
     * 模拟数据库查询凭证
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {

        User user = userDao.getUserByUserName(userName);

        if (user != null) {
            return user.getPassword();
        }

        return null;
    }

    // 测试加盐后的MD5码
    public static void main(String[] args) {
        Md5Hash md5Hash = new Md5Hash("1234567", "Mark");
        System.out.println(md5Hash.toString());
    }
}

