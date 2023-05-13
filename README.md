# REDIS PLAYGROUND W/ SPRING - SESSION
This repository groups examples of using [Redis](https://redis.io) with the [Spring Boot](https://spring.io/projects/spring-boot) framework. Redis is an in-memory, key-value NoSQL data store. Redis is often used for caching data, which reduces response-time of data intensive applications.

**This branch shows how to save client session to a Redis store.**

Different from other branches, this branch introduces a completely new application based on [Dan Vega's video on Spring Session](https://www.youtube.com/watch?v=k62bO-W6Sb0).

## Overview
An advantage of using Redis to save your session is that your session data is no longer tied to your server. Therefore, after an outage, your session data could be safely retrieved from a redis store. This is also an appropriate strategy when using distributed servers.

This application demonstrates this advantage by counting the number of requests to the root (`/`) endpoint, and saving the total count to a session attribute (`attr`). 

Even after restarting the application, the total count remains.


### The application
The whole application consists of a single controller ([`HomeController`](./src/main/java/com/example/redissession/HomeController.java)) that exposes two endpoints. 

The first endpoint is the root endpoint (`/`). It greets the user and increment a counter by calling the `incrementCount()` method.

The second endpoint (`/count`) shows the total number of requests registered to the root endpoint by retrieving the value from the user session.

The application also requires a configuration for starting the Redis store in the Docker container only once. Please checkout the [`application.properties`](./src/main/resources/application.properties) file.

## Dependencies
There are two sets of important dependencies in this project. The first set relates to the Redis implementation.

Notice that we have no particular code for using Redis. Yes, the dependencies do all the hard work for us. To do so, we need both `spring-boot-starter-data-redis` and `spring-session-data-redis`. Whereas the first dependency handles the access to a Redis store, the second handles the integration between session and store management operations.

The other important dependency is the `spring-boot-docker-compose`, responsible for starting the [`docker-compose.yml`](./docker-compose.yml) file. This file describes a Redis container for the application.

In addition, this project uses web and security dependencies.

> Please note that we are using Spring version 3.1.0-RC2, which requires a `repositories` session in the `pom.xml`.

## Running the Project Locally
To run the project, ensure that you have [Docker desktop installed and running](https://www.docker.com/products/docker-desktop/). You also need Git, Java 17, and Maven. Then, using your terminal, follow the steps below.

1. Clone the project locally

```
git clone https://github.com/gabrielcostasilva/sb-redis.git
```
2. Run the Spring Boot application. From the `sb-redis` folder type and run:

```
mvn spring-boot:run
```
3. Access the [application root](http://localhost:8080/) from your browser

4. Sign in to the application using Spring default user (`user`), and the password printed to the Spring console.

5. After accessing the application root a few times, [check the number of requests](http://localhost:8080/count)

6. Restart the application server and check that the total count remains the same.

> Optionally, you can check your Redis store connecting to the container and listing the session keys, like so:

```
docker exec -it redis-session-redis-1 redis-cli
keys *
```
