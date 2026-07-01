package com.hospital.redis.test;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RedisTestRunner implements CommandLineRunner {

    private final RedisTemplate<String,String> redisTemplate;

    @Override
    public void run(String... args) throws Exception {
//        redisTemplate.opsForValue()
//                .set("test", "hello");
//
//        System.out.println(
//                "Redis Value = "
//                        + redisTemplate.opsForValue()
//                        .get("test"));

    }
}
