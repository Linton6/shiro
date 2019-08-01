package com.imooc.controller;

import com.imooc.vo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @Author Linton
 * @Date 2019/7/31 20:18
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */
@Controller
public class UserController {

    @RequestMapping(value = "/subLogin", method = RequestMethod.POST ,
    produces = "application/json;charset=utf-8")
    @ResponseBody
    public String subLogin(User user) {
        // 获得主体
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(),user.getPassword());
        try {
            token.setRememberMe(user.isRememberMe());
            subject.login(token);
        } catch (AuthenticationException e) {
//            e.printStackTrace();
            return e.getMessage();
        }

        // 直接编码授权，还可以通过注解
        if (subject.hasRole("admin")){
            return "有admin权限";
        }

        return "无admin权限";
    }

//    @RequiresRoles("admin") // 当前主题必须具备admin这个角色才能访问这个方法
//    @RequiresPermissions("") // 当前主题必须具备括号内的权限才能访问这个方法
    @RequestMapping(value = "/testRole", method = RequestMethod.GET)
    @ResponseBody // 返回
    public String testRole() {
        return "testRole success";
    }

    @RequiresRoles("admin1")
    @RequiresPermissions("")
    @RequestMapping(value = "/testRole1", method = RequestMethod.GET)
    @ResponseBody // 返回
    public String testRole1() {
        return "testRole1 success";
    }

    @RequestMapping(value = "/testPerms", method = RequestMethod.GET)
    @ResponseBody // 返回
    public String testPerms() {
        return "testPerms success";
    }

    @RequestMapping(value = "/testPerms1", method = RequestMethod.GET)
    @ResponseBody // 返回
    public String testPerms1() {
        return "testPerms1 success";
    }
}

