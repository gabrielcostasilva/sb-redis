# sb-redis

docker network create -d bridge redisnet
docker run -d -p 6379:6379 --network redisnet redis
docker run -it --network redisnet --rm redis redis-cli -h 64709be6feaf

Redis playground with Spring

http post localhost:8080/person id=a89sd7f98a name=John age=33
http get localhost:8080/person/a89sd7f98a
http delete localhost:8080/person/a89sd7f98a