# sb-redis

docker network create -d bridge redisnet
docker run -d -p 6379:6379 --network redisnet redis
docker run -it --network redisnet --rm redis redis-cli -h 64709be6feaf

Redis playground with Spring
