package com.tool.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class SpringDataRedisTest {

    @Autowired
    RedisTemplate redisTemplate;


    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        //get redis operation tools for String/Hash/Set/ZSet/List
        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        ListOperations listOperations = redisTemplate.opsForList();
    }

    @Test
    public void testValueOperations() {
        redisTemplate.opsForValue().set("key", "value");

        redisTemplate.opsForValue().set("key2", "value2",3, TimeUnit.MINUTES);

        redisTemplate.opsForValue().setIfAbsent("key3", "value3");

        redisTemplate.opsForValue().setIfAbsent("key3", "value4");
    }
}
