package com.example.demo;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonRepository repository;

    @PostMapping
    public void save(@RequestBody Person person) {
        repository.save(person);

    }

    @GetMapping("/{id}")
    public Person find(@PathVariable final String id) {
        return repository.findById(id).get();

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable final String id) {
        repository.deleteById(id);

    }

}
