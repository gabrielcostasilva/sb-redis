# REDIS PLAYGROUND W/ SPRING - AUTO DOCKER COMPOSE
This repository groups examples of using [Redis](https://redis.io) with the [Spring Boot](https://spring.io/projects/spring-boot) framework. Redis is an in-memory, key-value NoSQL data store. Redis is often used for caching data, which reduces response-time of data intensive applications.

**This branch shows off a new Spring 3.1 feature that runs Docker compose files automatically.**

> If you want to understand the underlying application, please check out the [main branch](https://github.com/gabrielcostasilva/sb-redis.git).

## Overview
This project is the same as the [crud-repository](https://github.com/gabrielcostasilva/sb-redis/tree/crud-repository). However, the [`docker-compose.yml`](./docker-compose.yml) file in this project introduces a [new feature in Spring Boot 3.1](https://www.danvega.dev/blog/2023/04/26/spring-boot-docker-compose/). 

The `docker-compose.yml` file describes the containerised infrastructure necessary for running this project. Unlike the `crud-repository` project, you do not need to run a set of Docker commands to provisioning the infrastructure. Spring Boot takes care of everything thanks to `org.springframework.boot.spring-boot-docker-compose` dependency.

## Running the Project Locally
To run the project, ensure that you have [Docker desktop installed and running](https://www.docker.com/products/docker-desktop/). You also need Git, Java 17, and Maven. Then, using your terminal, follow the steps below.

1. Clone the project locally

```
git clone https://github.com/gabrielcostasilva/sb-redis.git
```

> Notice that you do not have to set up the infrastructure manually, like you have in other branches.

2. Run the Spring Boot application. From the `sb-redis` folder type and run:

```
mvn spring-boot:run
```

3. Use an HTTP/REST client to perform CRUD operations on the `Person` resource:

```
http post localhost:8080/person id=a89sd7f98a name=John age=33 
http get localhost:8080/person/a89sd7f98a
http delete localhost:8080/person/a89sd7f98a
```

> We use [HttPie](https://httpie.io) as our REST client.
