package com.example.demo;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class RedisListCache {

    private RedisTemplate<String, Object> redisTemplate;
    private ListOperations<String, Object> listOperations;
    
    public RedisListCache(RedisTemplate<String, Object> redisTemplate) {
    	this.redisTemplate = redisTemplate;
    	this.listOperations = redisTemplate.opsForList();
    }
    
    @PostConstruct
    public void setup() {
    	listOperations.leftPush("sentence", "Hello World!");
    	
    	System.out.println(listOperations.rightPop("sentence"));
    }
}