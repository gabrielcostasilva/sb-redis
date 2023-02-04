# sb-redis

docker network create -d bridge redisnet
docker run -d -p 6379:6379 --network redisnet redis
docker run -it --network redisnet --rm redis redis-cli -h 64709be6feaf

Redis playground with Spring

http post localhost:8080/person id=a89sd7f98a name=John age=33
http get localhost:8080/person/a89sd7f98a
http delete localhost:8080/person/a89sd7f98a

https://www.javacodemonk.com/custom-ttl-for-spring-data-redis-cache-6b38c550
https://www.youtube.com/watch?v=GtZ3W7g9Arw

https://www.digitalocean.com/community/tutorials/spring-boot-redis-cache