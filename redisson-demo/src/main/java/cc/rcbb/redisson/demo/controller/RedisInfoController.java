package cc.rcbb.redisson.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;

@RestController
@RequestMapping("/redis")
public class RedisInfoController {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @GetMapping("/info")
    public Properties getRedisInfo() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return connection.info();  // 获取 Redis 配置及状态信息
        }
    }

    @GetMapping("/config")
    public Properties getRedisConfig() {
        try (RedisConnection connection = redisConnectionFactory.getConnection()) {
            return connection.getConfig("*"); // 获取所有 Redis 配置项
        }
    }
}
