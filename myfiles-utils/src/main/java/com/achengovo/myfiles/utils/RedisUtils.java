package com.achengovo.myfiles.utils;

import redis.clients.jedis.Jedis;

/**
 * Redis工具类
 */
public class RedisUtils {
    //连接本地的 Redis 服务
    static Jedis jedis = new Jedis("localhost");

    /**
     * String放入redis
     * @param key
     * @param value
     * @return
     */
    public static boolean set(String key, String value) {
        try {
            jedis.set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * String放入redis并设置过期时间
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public static boolean set(String key, String value, int seconds) {
        try {
            jedis.set(key, value);
            jedis.expire(key, seconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Object放入redis
     * @param key
     * @param value
     * @return
     */
    public static boolean setObject(String key, Object value) {
        try {
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Object放入redis并设置过期时间
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    public static boolean setObject(String key, Object value, int seconds) {
        try {
            jedis.set(key.getBytes(), SerializeUtil.serialize(value));
            jedis.expire(key.getBytes(), seconds);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从redis获取Object
     * @param key
     * @return
     */
    public static Object getObject(String key) {
        try {
            byte[] result=jedis.get(key.getBytes());
            if(result==null){
                return null;
            }
            return SerializeUtil.unserialize(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从redis获取String
     * @param key
     * @return
     */
    public static String get(String key) {
        return jedis.get(key);
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public static boolean del(String key){
        if(jedis.del(key)==1){
            return true;
        }
        return false;
    }
}
