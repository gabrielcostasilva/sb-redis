# REDIS PLAYGROUND W/ SPRING - CRUD CACHE
This repository groups examples of using [Redis](https://redis.io) with the [Spring Boot](https://spring.io/projects/spring-boot) framework. Redis is an in-memory, key-value NoSQL data store. Redis is often used for caching data, which reduces response-time of data intensive applications.

**This branch presents the caching feature with Redis**.

> If you want to understand the underlying application, please check out the [main branch](https://github.com/gabrielcostasilva/sb-redis.git).

## Overview
[`main`](https://github.com/gabrielcostasilva/sb-redis), [`crud`](https://github.com/gabrielcostasilva/sb-redis/tree/crud), and [`crud-repository`](https://github.com/gabrielcostasilva/sb-redis/tree/crud-repository) branches show examples of managing a Redis store. However, a popular use of Redis is for caching data from a SQL database. 

As the project in the [`crud-repository`](https://github.com/gabrielcostasilva/sb-redis/tree/crud-repository) branch, this project takes advantage of Spring Data auto-configuration. Therefore, there is no need for a configuration class or properties. However, we do set some properties for caching, as presented below. 

```
spring.cache.type=redis
spring.cache.redis.time-to-live=3000
```
These properties are defined in the [`application.properties` file](./src/main/resources/application.properties). The first line sets Redis as the data store for caching whereas the second line sets how long a cache entry lasts (ms).

To persist data in a SQL database, we use JPA. Therefore, [`Person`](./src/main/java/com/example/demo/Person.java) and [`PersonRepository`](./src/main/java/com/example/demo/PersonRepository.java) implement an entity and a repository/DAO pattern, respectively.

[`PersonController`](./src/main/java/com/example/demo/PersonController.java) implements a REST API that saves and lists `Person`.

[`PersonService`](./src/main/java/com/example/demo/PersonService.java) plays the key role for caching data. The code snippet below shows the `findAll()` method.

```java
@Cacheable(value = "persons") // (1)
public List<Person> findAll() {
    log.info("Querying database ..."); // (2)
    return repository.findAll(); // (3)
}
```
The `org.springframework.cache.annotation.Cacheable` annotation caches data from the `findAll()` method (1). When a request reaches the method, a log records whether the method is called (2). 

If this is the first call to the method, or the time-to-live has expired, data is retrieved from the database (3). Otherwise, the cache is used.

One can inspect the system log to understand when the method is called.

A final detail is that the main class ([`DemoApplication`](./src/main/java/com/example/demo/DemoApplication.java)) needs to enable caching. To do so, add the `org.springframework.cache.annotation.EnableCaching` annotation just above the class declaration. 

## Dependencies
This project uses H2 database, and Spring Data JPA, in addition to Spring Data Redis (of course ...).

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

## Additional References
- [Custom TTL for Spring data Redis Cache](https://www.javacodemonk.com/custom-ttl-for-spring-data-redis-cache-6b38c550) explains possible values for Redis properties

- [How to use Redis with Spring Caching](https://www.youtube.com/watch?v=GtZ3W7g9Arw) shows how to use `@Cacheable`

- [Spring Boot Redis Cache](https://www.digitalocean.com/community/tutorials/spring-boot-redis-cache) features ways to extend this project with other useful annotations. **Note that this tutorial shows an interesting pattern by caching the API rather than the data repository**.
