package com.imooc.dao;

import com.imooc.vo.User;

import java.util.List;

/**
 * @Author Linton
 * @Date 2019/8/1 8:14
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 */

public interface UserDao {
    User getUserByUserName(String userName);

    List<String> queryRolesByUserName(String userName);
}

