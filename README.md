# REDIS PLAYGROUND W/ SPRING
This repository groups examples of using [Redis](https://redis.io) with the [Spring Boot](https://spring.io/projects/spring-boot) framework. Redis is an in-memory, key-value NoSQL data store. Redis is often used for caching data, which reduces response-time of data intensive applications.

This main branch introduces the Redis setup with [Docker](https://www.docker.com), and shows how to put and get data to/from Redis using lists.

Most code in this repository is inspired by [Lilium Code](https://www.youtube.com/@liliumcode6666) playlist on [Redis with Spring Boot](https://youtube.com/playlist?list=PLXy8DQl3058PrUKdnSn6e49Cx-rvT3Kv3).

## Other Branches

This original project is extended in other branches, as follows:

- [crud](https://github.com/gabrielcostasilva/sb-redis/tree/crud) implements a basic CRUD for a Redis data store.
- [crud-repository](https://github.com/gabrielcostasilva/sb-redis/tree/crud-repository) extends the basic CRUD using Spring traditional `Repository` interface.
- [crud-cache](https://github.com/gabrielcostasilva/sb-redis/tree/crud-cache) uses Redis as a cache between a SQL database and a web application.
- [session](https://github.com/gabrielcostasilva/sb-redis/tree/session) saves the current session to a Redis store. It also uses [Spring Session project](https://docs.spring.io/spring-session/reference/index.html).

## Overview
This project consists of three classes. [`DemoApplication`](./src/main/java/com/example/demo/DemoApplication.java) has no additional code other than the code automatically created for starting the application.

### `RedisConfiguration`

[`RedisConfiguration`](./src/main/java/com/example/demo/RedisConfiguration.java) is a configuration class. Spring Boot uses this class to set the store connection.

The code below shows the full `RedisConfiguration` class.

```java
@Configuration
public class RedisConfiguration {

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() { // (1)
        final RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(); // (2)
        config.setHostName("localhost"); // (3)
        config.setPort(6379); // (4)

        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() { // (5)
        final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        return redisTemplate;
    }
}
```

In its essence, Redis is a database. Therefore, you need to connect to it as any other database. Spring Redis data dependency provides connection interfaces and implementations to enabling connecting to a Redis store.

Our configuration uses the [Lettuce](https://github.com/lettuce-io/lettuce-core) connector to connect to the Redis store (1). To do so, we set the store location (3-4) as part of Redis configuration (2).

Next, we set how the interaction between the application and the store is going to happen (5). We use a `RedisTemplate` object as it _"takes care of serialization and connection management, freeing the user from dealing with such details"_ [1].

The `RedisTemplate` object also provides access to operations that one can perform on the Redis store, as we explain later.

### RedisListCache
The [`RedisListCache`](./src/main/java/com/example/demo/RedisListCache.java) uses the configuration previously defined to insert and retrieve data to the Redis store.

The code below fully shows the `RedisListCache` class.

```java
@Service
@RequiredArgsConstructor // (1)
public class RedisListCache {

    private final RedisTemplate<String, Object> redisTemplate; // (1)

    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> listOperations;
        
    @PostConstruct
    public void setup() {
    	listOperations.leftPush("sentence", "Hello World!"); // (2)
    	
    	System.out.println(listOperations.rightPop("sentence")); // (3)
    }
}
```
First, we use a Lombok annotation to inject the `RedisTemplate` object into the code (1). Then, we inject a `ListOperations` object from the `RedisTemplate` object. A `ListOperations` is an object that enables performing ... well ... list operations on the data store!

A list operation creates a list of items identified by a key. The code above shows a key (`sentence`) and a value (`Hello World!`) that are added to a list with a `leftPush` operation (2). If we repeat the operation with another value, we would have something like this: key="sentence", value=["another value", "Hello World!"]. That is because the `leftPush` operation adds content to the left of the list.

Then, we retrieve the data with the `rightPop` operation (3). The returned value is printed to the console.

## Dependencies
This project uses Web and Redis Spring Boot starters, in addition to the [Lombok library](https://projectlombok.org). 

> When creating the project, I chose Reactive Redis dependency by mistake. It worked as expected, but notice there are two Redis dependencies when using [start.spring.io](https://start.spring.io).

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

7. The application does not have an GUI, but you should see the  `"Hello World!"` message on your screen.

## Additional References
- [Spring Data Redis](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#introduction) is the official documentation for using Redis with Spring through Spring Data.

[1] https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#redis:template