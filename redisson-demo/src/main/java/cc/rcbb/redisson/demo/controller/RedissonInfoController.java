package cc.rcbb.redisson.demo.controller;

import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.redisson.api.RedissonClient;

import java.io.IOException;

@RestController
@RequestMapping("/redisson")
public class RedissonInfoController {

    @Autowired
    private RedissonClient redissonClient;


    @GetMapping("/config")
    public String getRedissonConfig() throws IOException {
        Config config = redissonClient.getConfig();
        return config.toJSON().toString(); // 获取 Redisson 配置（JSON 格式）
    }
}
