package com.jl.db.utils.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class RedisCluster implements RedisClient {

    private static final String LOCK_SUCCESS = "OK";

    private static final String SET_IF_NOT_EXIST = "NX";//只有key 不存在时才把key value set 到redis
    private static final String SET_IF_EXIST = "XX";//只有 key 存在是，才把key value set 到redis 用于跟新

    private static final String SET_WITH_EXPIRE_MILLI_SECONDS = "PX";//毫秒
    private static final String SET_WITH_EXPIRE_SECONDS = "EX";//秒
    private static final Long RELEASE_SUCCESS = 1L;

    @Resource
    private JedisCluster jedisCluster;

    @Override
    public boolean set(String key, Object value) {
        String s = jedisCluster.set(key, JSON.toJSONString(value));
        if ("OK".equals(s)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean set(String key, Object value, long timeOut, TimeUnit unit) {
        boolean s = set(key, value);
        if (s) {
            expire(key, timeOut, unit);
        }
        return s;
    }

    @Override
    public Boolean expire(String key, long timeOut, TimeUnit unit) {
        int seconds = 0;
        if (unit == null || TimeUnit.SECONDS.equals(unit)) {
            seconds = (int) timeOut;
        } else if (TimeUnit.DAYS.equals(unit)) {
            seconds = (int) TimeUnit.DAYS.toSeconds(timeOut);
        } else if (TimeUnit.HOURS.equals(unit)) {
            seconds = (int) TimeUnit.HOURS.toSeconds(timeOut);
        } else if (TimeUnit.MINUTES.equals(unit)) {
            seconds = (int) TimeUnit.MINUTES.toSeconds(timeOut);
        } else if (TimeUnit.MILLISECONDS.equals(unit)) {
            Long flag = jedisCluster.pexpire(key, timeOut);
            return flag != null && flag > 0 ? true : false;
        }
        Long flag = jedisCluster.expire(key, seconds);
        return flag != null && flag > 0 ? true : false;
    }

    @Override
    public Boolean expireAt(String key, long millisecondsTimestamp) {
        Long flag = jedisCluster.expireAt(key, millisecondsTimestamp);
        return flag != null && flag > 0 ? true : false;
    }

    @Override
    public boolean exit(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public String get(String key) {
        return get(key, String.class);
    }

    @Override
    public <T> T get(String key, Class<T> clazz) {
        String value = jedisCluster.get(key);
        if (value == null) return null;
        return JSON.parseObject(value, clazz);
    }

    @Override
    public <T> List<T> getList(String key, Class<T> clazz) {
        String value = jedisCluster.get(key);
        if (value == null) return null;
        return JSONArray.parseArray(value, clazz);
    }

    @Override
    public Boolean delete(String key) {
        Long flag = jedisCluster.del(key);
        return flag != null && flag > 0 ? true : false;
    }

    @Override
    public Set<String> getKeys(String pattern) {
        String key = pattern + "*";
        Set<String> keys = new HashSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            Jedis connection = clusterNodes.get(k).getResource();
            try {
                keys.addAll(connection.keys(key));
            } catch (Exception e) {
                log.error("Getting keys error: {}", e);
            } finally {
                connection.close();//用完一定要close这个链接！！！
            }
        }
        log.info("模糊匹配key", Arrays.asList(keys));
        return keys;
    }

    @Override
    public void delAll(String pattern) {
        //Set<String> keys = getKeys(pattern);
        Set<String> keys = clusterScan(pattern);
        for (String key : keys) {
            jedisCluster.del(key);//集群模式下只能一个个删除
        }
    }

    @Override
    public Set<String> clusterScan(String pattern) {
        Set<String> keys = new HashSet<>();
        ScanParams scanParams = new ScanParams();
        scanParams.match(pattern + "*");
        scanParams.count(1000);

        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for (String node : clusterNodes.keySet()) {
            Jedis connection = clusterNodes.get(node).getResource();
            try {
                String cursor = ScanParams.SCAN_POINTER_START;
                while (true) {
                    ScanResult<String> scan = connection.scan(cursor, scanParams);
                    List<String> result = scan.getResult();
                    if (!result.isEmpty()) {
                        log.info(pattern + "模糊匹配redis的key={}存在的node={}", result
                                , connection.getClient().getHost() + ":" + connection.getClient().getPort());
                        keys.addAll(result);
                    }
                    cursor = scan.getStringCursor();
                    if ("0".equals(cursor)) {
                        break;
                    }
                }
            } catch (Exception e) {
                log.error("clusterScan getting keys error: {}", e);
            } finally {
                connection.close();//用完一定要close这个链接！！！
            }
        }
        return keys;
    }

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey    锁
     * @param requestId  请求标识
     * @param expireTime 超期时间 秒
     * @return 是否获取成功
     */
    @Override
    public boolean setDistributedLock(String lockKey, String requestId, long expireTime) {
        String result = jedisCluster.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_SECONDS, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    /**
     * 释放分布式锁
     *
     * @param lockKey   锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    @Override
    public boolean releaseDistributedLock(String lockKey, String requestId) {
        try {
            String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
            Object result = jedisCluster.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
            if (RELEASE_SUCCESS.equals(result)) {
                return true;
            }
        } catch (Exception e) {
            log.info("releaseDistributedLock失败：", e);
        }
        return false;
    }
}
