package com.example.demo;

import java.io.Serializable;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

record Person(String id, String name, int age) implements Serializable  { }

@RestController
@RequestMapping("/person")
public class PersonController {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final ValueOperations<String, Object> valueOperations;

    PersonController(final RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();

    }

    @PostMapping
    public void save(@RequestBody Person person) {
        valueOperations.set(person.id(), person);

    }

    @GetMapping("/{id}")
    public Person find(@PathVariable final String id) {
        return (Person) valueOperations.get(id);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final String id) {
        valueOperations.getOperations().delete(id);

    }

}
