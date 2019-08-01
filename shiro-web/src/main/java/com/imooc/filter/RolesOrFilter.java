package com.imooc.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;


/**
 * @Author Linton
 * @Date 2019/8/1 9:47
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */

public class RolesOrFilter extends AuthorizationFilter {
    @Override
    protected boolean isAccessAllowed(javax.servlet.ServletRequest servletRequest, javax.servlet.ServletResponse servletResponse, Object o) throws Exception {
// Object o 就是角色数组
        Subject subject = getSubject(servletRequest, servletResponse);
        String[] roles = (String[]) o;
        if (roles == null || roles.length == 0) {
            return true;
        }
        for (String role : roles) {
            if (subject.hasRole(role))
                return true;
        }

        return false;
    }
}

