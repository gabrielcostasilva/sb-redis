# REDIS PLAYGROUND W/ SPRING - CRUD REPOSITORY
This repository groups examples of using [Redis](https://redis.io) with the [Spring Boot](https://spring.io/projects/spring-boot) framework. Redis is an in-memory, key-value NoSQL data store. Redis is often used for caching data, which reduces response-time of data intensive applications.

**This branch shows Spring Data features with a Redis store using the `Repository` interface.** [Spring Data](https://spring.io/projects/spring-data) is a project under the huge umbrella of the Spring framework. It provides a consistent abstraction layer for managing data stores.

Spring Data supports a variety of data stores. To do so, it provides specific implementations, such as [Spring Data JPA](https://spring.io/projects/spring-data-jpa), [Spring Data MongoDB](https://spring.io/projects/spring-data-mongodb), and [Spring Data Redis](https://spring.io/projects/spring-data-redis).

> If you want to understand the underlying application, please check out the [main branch](https://github.com/gabrielcostasilva/sb-redis.git).

## Overview
Unlike the project in the main repository, this project takes advantage of the Spring auto-configuration for setting our Redis store. Therefore, there is no need for a `RedisConfiguration` class as before.

Also, whereas projects in both the `main` and the [`crud`](https://github.com/gabrielcostasilva/sb-redis/tree/crud) branches use specific objects for managing the data store, this project uses the Spring [repository/DAO pattern](http://www.corej2eepatterns.com/DataAccessObject.htm).

Thus, this project introduces both the [`Person` Java record](./src/main/java/com/example/demo/Person.java) and the [`PersonRepository` interface](./src/main/java/com/example/demo/PersonRepository.java). Whereas the `Person` Java record models the the entity data, the `PersonRepository` interface provides CRUD operations over `Person` data.

The [`Person` Java record](./src/main/java/com/example/demo/Person.java) defines the attributes of a `Person`. In addition, it uses `org.springframework.data.redis.core.RedisHash` annotation. According to the official documentation, it _"marks Objects as aggregate roots to be stored in a Redis hash"_. The explanation comes [from the documentation](https://docs.spring.io/spring-data/redis/docs/current/reference/html/#redis.hashmappers.root), _"Data can be stored by using various data structures within Redis"_.

The [`PersonRepository` interface](./src/main/java/com/example/demo/PersonRepository.java) just extends the traditional `CrudRepository` interface to providing CRUD operations. 

As result of using an extension of `CrudRepository`, [`PersonController`](./src/main/java/com/example/demo/PersonController.java) injects `PersonRepository` and uses it to save, retrieve and delete data direct into the Redis store. 

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
- [Introduction to Spring Data Redis](https://www.baeldung.com/spring-data-redis-tutorial) provides a similar example to that used in this branch.
