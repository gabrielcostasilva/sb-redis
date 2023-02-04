package com.example.demo;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisHash;

@RedisHash("Person")
record Person(String id, String name, int age) implements Serializable  { }
