package com.imooc.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @Author Linton
 * @Date 2019/8/1 11:01
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description
 *
 */
@Component
public class JedisUtil {

    @Autowired
    private JedisPool jedisPool; // jedisPool获取连接  在spring.xml中引入和spring-redis.xml文件

    private Jedis getResource(){
        return jedisPool.getResource();
    }

    public byte[] set(byte[] key, byte[] value) {
        Jedis jedis = getResource();
        try{
            jedis.set(key,value);
            return value;
        } finally {
            jedis.close();
        }

    }

    public void expire(byte[] key, int i) {
        Jedis jedis = getResource();
        try{
            jedis.expire(key,i);
        } finally {
            jedis.close();
        }
    }

    public byte[] get(byte[] key) {
        Jedis jedis = getResource();
        try{
            return  jedis.get(key);
        } finally {
            jedis.close();
        }
    }

    public void del(byte[] key) {
        Jedis jedis = getResource();
        try{
            jedis.del(key);
        } finally {
            jedis.close();
        }
    }

    public Set<byte[]> keys(String shiro_session_prefix) {
        Jedis jedis = getResource();
        try{
           return jedis.keys((shiro_session_prefix + "*").getBytes());
        } finally {
            jedis.close();
        }
    }
}

