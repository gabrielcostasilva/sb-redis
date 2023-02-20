package com.example.demo;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;

@Service
@RequiredArgsConstructor
@Log
public class PersonService {
    
    private final PersonRepository repository;

    @Cacheable(value = "persons")
    public List<Person> findAll() {
        log.info("Querying database ...");
        return repository.findAll();
    }

    public Person save(Person aPerson) {
        return repository.save(aPerson);
    }
}
