package com.jl.db.utils.redis;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisClient {

    /**
     * 存数据
     */
    boolean set(String key, Object value);

    /**
     * 存数据
     *
     * @param key
     * @param value
     * @param timeOut
     * @param unit
     * @return
     */
    boolean set(String key, Object value, long timeOut, TimeUnit unit);

    /**
     * 设置超时时间
     *
     * @param key
     * @param timeOut
     * @param unit
     * @return
     */
    Boolean expire(String key, long timeOut, TimeUnit unit);

    /**
     * 具体的实效时间
     * @param key
     * @param millisecondsTimestamp 时间点的时间戳
     * @return
     */
    Boolean expireAt(String key, long millisecondsTimestamp);

    /**
     * 是否存在key
     * @param key
     * @return
     */
    boolean exit(String key);

    /**
     * 获取单个字符串对象
     */
    String get(String key);

    /**
     * 获取单个对象
     */
    <T> T get(String key, Class<T> clazz);

    /**
     * 获取集合对象
     */
    <T> List<T> getList(String key, Class<T> clazz);

    /**
     * 删除KEY
     *
     * @param key
     * @return
     */
    Boolean delete(String key);

    /**
     * 模糊匹配key
     *
     * @param pattern
     * @return
     */
    Set<String> getKeys(String pattern);

    /**
     * 删除所有模糊匹配到的key
     *
     * @param pattern
     */
    void delAll(String pattern);

    /**
     * 模糊匹配key scan比keys性能更高
     *
     * @param pattern
     * @return
     */
    Set<String> clusterScan(String pattern);

    /**
     * @param lockKey
     * @param requestId
     * @param expireTime
     * @return
     */
    boolean setDistributedLock(String lockKey, String requestId, long expireTime);

    /**
     * @param lockKey
     * @param requestId
     * @return
     */
    boolean releaseDistributedLock(String lockKey, String requestId);

}
