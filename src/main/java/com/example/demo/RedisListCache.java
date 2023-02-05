package com.example.demo;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisListCache {

    private final RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> listOperations;
        
    @PostConstruct
    public void setup() {
    	listOperations.leftPush("sentence", "Hello World!");
    	
    	System.out.println(listOperations.rightPop("sentence"));
    }
}