# REDIS PLAYGROUND W/ SPRING - CRUD
This repository groups examples of using [Redis](https://redis.io) with the [Spring Boot](https://spring.io/projects/spring-boot) framework. Redis is an in-memory, key-value NoSQL data store. Redis is often used for caching data, which reduces response-time of data intensive applications.

**This branch introduces CRUD operation on a Redis store using the `ValueOperations` object.**

> If you want to understand the underlying application, please check out the [main branch](https://github.com/gabrielcostasilva/sb-redis.git).

## Overview
Like the main repository, this uses [RedisConfiguration](./src/main/java/com/example/demo/RedisConfiguration.java) class to set Redis configuration. Please check out the explanation in the [main branch](https://github.com/gabrielcostasilva/sb-redis.git) if you are interested in understanding how the configuration works.

Unlike the main repository, this introduces a REST API as a way to show CRUD operations on a Redis store. Please check out the `rest` folder in [this repository](https://github.com/gabrielcostasilva/sb-controllers.git) if you want to understand how to create REST APIs with Spring Boot.

[`PersonController`](./src/main/java/com/example/demo/PersonController.java) class injects the `org.springframework.data.redis.core.ValueOperations` object. This object provides methods for storing values as `String`.

In contrast with the main branch, this project instantiates `ValueOperations` using the constructor instead of the `Resource` annotation. Note that the `ValueOperations` is extracted from `RedisTemplate` (1). As before, `RedisTemplate` is injected in the constructor.

```java
(...)

PersonController(final RedisTemplate<String, Object> redisTemplate) {
    this.redisTemplate = redisTemplate;
    this.valueOperations = redisTemplate.opsForValue(); // (1)

}

(...)
```

CRUD operations use `get` and `set` operations to retrieve and create/update values, like so.

```java
(...)

valueOperations.set(person.id(), person);

(...)

return (Person) valueOperations.get(id);

(...)
```
However, the `ValueOperations` object does not provide a direct method for deleting items. Therefore, we use the `getOperations()` method to access an implementation of `RedisOperations` interface. 

The `RedisOperations` interface provides the method `delete(key)`, which enables deleting an item based on its key, like the snippet below shows.

```java
(...)

valueOperations.getOperations().delete(id);

(...)
```

## Running the Project Locally
To run the project, ensure that you have [Docker desktop installed and running](https://www.docker.com/products/docker-desktop/). You also need Git, Java 17, and Maven. Then, using your terminal, follow the steps below.

1. Clone the project locally

```
git clone https://github.com/gabrielcostasilva/sb-redis.git
```

2. Create a network to use with Docker

```
docker network create -d bridge redisnet
```

3. Run the docker server

```
docker run -d -p 6379:6379 --network redisnet --name redis-server redis
```

4. Start the Redis client

```
docker run -it --network redisnet --rm redis redis-cli -h redis-server
```

5. Test whether Redis is working by typing the following commands into the Redis client console

```
ping
(It should respond with PONG)

set name "John Doe"
(It creates a key called __name__, with a value of "John Doe")

get name
(It should respond with "John Doe")
```
6. Run the Spring Boot application. From the `sb-redis` folder type and run:

```
mvn spring-boot:run
```

7. Use an HTTP/REST client to perform CRUD operations on the `Person` resource:

```
http post localhost:8080/person id=a89sd7f98a name=John age=33 
http get localhost:8080/person/a89sd7f98a
http delete localhost:8080/person/a89sd7f98a
```

> We use [HttPie](https://httpie.io) as our REST client.
