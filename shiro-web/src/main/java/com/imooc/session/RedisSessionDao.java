package com.imooc.session;

import com.imooc.util.JedisUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Linton
 * @Date 2019/8/1 10:58
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description 通过Redis实现session共享，主要是重写它的增删改查方法
 * AbstractSessionDAO 主要方法:增删改成，获取主要活动的session
 */

public class RedisSessionDao extends AbstractSessionDAO {

    @Resource
    private JedisUtil jedisUtil;

    private final String SHIRO_SESSION_PREFIX = "imooc-session:";

    private byte[] getKey(String key) { // redis 中数据以二进制存储
        return (SHIRO_SESSION_PREFIX + key).getBytes();
    }

    private void saveSession(Session session) {
        if (session != null && session.getId() != null) {
            byte[] key = getKey(session.getId().toString());
            byte[] value = SerializationUtils.serialize(session);
            jedisUtil.set(key, value); // 存入缓存
            jedisUtil.expire(key, 600); // 设置过期时间
        }
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = generateSessionId(session);
        assignSessionId(session, sessionId); // 将session和sessionId进行捆绑
        saveSession(session);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        System.out.println("read session ");
        if (sessionId == null){
            return null;
        }
        byte[] key = getKey(sessionId.toString());
        byte[] value = jedisUtil.get(key);
        return (Session)SerializationUtils.deserialize(value); // 把value数组反序列化成Session对象
    }

    @Override
    public void update(Session session) throws UnknownSessionException {
        saveSession(session);
    }

    @Override
    public void delete(Session session) {
        if (session == null || session.getId() == null){
            return;
        }
        byte[] key = getKey(session.getId().toString());
        jedisUtil.del(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<byte[]> keys = jedisUtil.keys(SHIRO_SESSION_PREFIX);
        Set<Session> sessions = new HashSet<Session>();
        if (CollectionUtils.isEmpty(keys)) { // 如果这个集合是空的，那就直接返回这个空集合
            return sessions;
        }
        for (byte[] key :keys){
            Session session = (Session)SerializationUtils.deserialize(jedisUtil.get(key));// 把value数组反序列化成Session对象，存储到集合中
            sessions.add(session);
        }
        return sessions;
    }
}

