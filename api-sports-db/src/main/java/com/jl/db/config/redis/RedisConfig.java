package com.jl.db.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;


import com.jl.db.utils.redis.RedisClient;
import com.jl.db.utils.redis.RedisCluster;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
public class RedisConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public RedisClient redisClient(){
        return new RedisCluster();
    }

    /**
     * @Description 创建集群配置bean
     */
    @Bean
    public JedisCluster jedisCluster() {
        // 切割配置的redis集群节点
        int timeOut = (int) redisProperties.getTimeout().toMillis();
        List<String> nodeList = redisProperties.getCluster().getNodes();
        Set<HostAndPort> nodes = new HashSet<>();
        for (String ipPort : nodeList) {
            String[] ipPortPair = ipPort.split(":");
            String host = ipPortPair[0].trim();
            int port = Integer.parseInt(ipPortPair[1].trim());
            nodes.add(new HostAndPort(host, port));
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(redisProperties.getJedis().getPool().getMaxIdle());
        config.setMinIdle(redisProperties.getJedis().getPool().getMinIdle());
        //连接池的最大数据库连接数
        config.setMaxTotal(redisProperties.getJedis().getPool().getMaxActive());
        config.setMaxWaitMillis(redisProperties.getJedis().getPool().getMaxWait().toMillis());
        config.setTestWhileIdle(true);
        //连接的最小空闲时间 默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(300000);
        //每次释放连接的最大数目,默认3
        config.setMinEvictableIdleTimeMillis(5);
        //逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        config.setTimeBetweenEvictionRunsMillis(30000);
        config.setTestOnBorrow(true);
        return new JedisCluster(nodes, timeOut, timeOut, redisProperties.getCluster().getMaxRedirects(), redisProperties.getPassword(), config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        //在spring-boot-starter-web包里
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);


        RedisTemplate redisTemplate = new RedisTemplate();
        //如果不配置Serializer，那么存储的时候缺省使用String，如果用User类型存储，那么会提示错误User can't cast to String！
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        // 开启事务
        redisTemplate.setEnableTransactionSupport(true);
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        return redisTemplate;
    }

}