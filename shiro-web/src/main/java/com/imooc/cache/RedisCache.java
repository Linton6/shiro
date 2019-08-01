package com.imooc.cache;

import com.imooc.util.JedisUtil;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.stereotype.Component;
import org.springframework.util.SerializationUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Set;

/**
 * @Author Linton
 * @Date 2019/8/1 20:14
 * @Email lintonhank@foxmail.com
 * @Version 1.0
 * @Description  对于缓存的增删改查
 */
@Component
public class RedisCache<K, V> implements Cache<K, V> {

    @Resource
    private JedisUtil jedisUtil;

    private final String CACHE_PREFIX = "imooc-cache";

    private byte[] getKey(K k) {
        if (k instanceof  String) {
            return (CACHE_PREFIX + k).getBytes();
        }
        return SerializationUtils.serialize(k); //直接将k序列化
    }

    @Override
    public V get(K k) throws CacheException {
        System.out.println("从redis获取权限数据");
        byte[] value = jedisUtil.get(getKey(k));
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public V put(K k, V v) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = SerializationUtils.serialize(v); // 把key和value序列化为字节数组
        jedisUtil.set(key, value);
        jedisUtil.expire(key, 600);
        return v;
    }

    @Override
    public V remove(K k) throws CacheException {
        byte[] key = getKey(k);
        byte[] value = jedisUtil.get(key);
        jedisUtil.del(key);
        if (value != null) {
            return (V) SerializationUtils.deserialize(value);
        }
        return null;
    }

    @Override
    public void clear() throws CacheException {
        //
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set keys() {
        return null;
    }

    @Override
    public Collection values() {
        return null;
    }
}

