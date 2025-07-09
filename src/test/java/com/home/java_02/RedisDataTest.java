package com.home.java_02;

import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
public class RedisDataTest {

  private static final Logger log = LoggerFactory.getLogger(RedisDataTest.class);

  @Autowired
  private Jedis jedis;

  @Test
  void redisStringExample() {

    // api : /api/users/22 <- 해당 api 호출 응답값 캐싱
    jedis.set("user:22:session", "{\"id\" : 22, \"name\" : \"김철수\"}");
    jedis.expire("user:22:session", 60 * 60);

    String response = jedis.get("user:22:session");
    log.info("/api/users/22 요청에 따른 캐싱된 응답 값 : {}", response);

    Long ttl = jedis.ttl("user:22:session");
    log.info("ttl: {}", ttl);

    jedis.set("article:101:views", "0");
    log.info("article:101:views: {}", jedis.get("article:101:views"));//0

    jedis.incr("article:101:views");
    jedis.incrBy("article:101:views", 10);
    log.info("article:101:views: {}", jedis.get("article:101:views"));//11

    jedis.decr("article:101:views");
    log.info("article:101:views: {}", jedis.get("article:101:views"));//10


  }

  @Test
  void redisListExample() {
    jedis.del("queue:tasks");
    jedis.lpush("queue:tasks", "task1", "task2", "task3", "task4", "task5", "task6");
    Long queueSize = jedis.llen("queue:tasks");
    log.info("queueSize: {}", queueSize);

    String lTask = jedis.lpop("queue:tasks");
    log.info("task: {}", lTask);

    String rTask = jedis.rpop("queue:tasks");
    log.info("task: {}", rTask);

    jedis.del("queue:tasks");
    jedis.rpush("queue:tasks", "task1", "task2", "task3", "task4", "task5", "task6");
    Long queueSize2 = jedis.llen("queue:tasks");
    log.info("queueSize: {}", queueSize2);

    String lTask2 = jedis.lpop("queue:tasks");
    log.info("task: {}", lTask2);

    String rTask2 = jedis.rpop("queue:tasks");
    log.info("task: {}", rTask2);
  }

  @Test
  void redisSetExample() {
    jedis.del("set1:tasks");
    jedis.del("set2:tasks");

    jedis.sadd("set1:tasks", "task1", "task2", "task3", "task4");
    jedis.sadd("set2:tasks", "task3", "task4", "task5", "task6");

    Set<String> sinterSet = jedis.sinter("set1:tasks", "set2:tasks");
    log.info("sinterSet: {}", sinterSet);

    Set<String> sunionSet = jedis.sunion("set1:tasks", "set2:tasks");
    log.info("sunionSet: {}", sunionSet);
  }

  @Test
  void redisHashExample() {
    jedis.hset("user:123", "name", "김철수");
    jedis.hset("user:123", "age", "18");
    jedis.hset("user:123", "email", "test@test.com");
    jedis.hset("user:123", "city", "seoul");

    String name = jedis.hget("user:123", "name");
    Map<String, String> hget = jedis.hgetAll("user:123");
    log.info("java map : {}, redis : {}", hget.get("name"), name);
  }

  @Test
  void redisSortedSetExample() {
    jedis.del("user:123:friendly");

    jedis.zadd("user:123:friendly", 100, "칭구1");
    jedis.zadd("user:123:friendly", 200, "칭구2");
    jedis.zadd("user:123:friendly", 300, "칭구3");
    jedis.zadd("user:123:friendly", 400, "칭구4");

    List<String> friends = jedis.zrevrange("user:123:friendly", 0, 2); // 오름차순
    log.info("friends: {}", friends);

    List<String> friends2 = jedis.zrevrange("user:123:friendly", -2, 1);//[]
    log.info("friends2: {}", friends2);

    List<String> friends3 = jedis.zrange("user:123:friendly", 1, 3); // 내림차순
    log.info("friends3: {}", friends3);

    Double score = jedis.zscore("user:123:friendly", "칭구1");
    log.info("score: {}", score);

    jedis.zincrby("user:123:friendly", 120, "칭구1"); // 데이터 베이스에 기준 값 업데이트 시점

    Double score1 = jedis.zscore("user:123:friendly", "칭구1");
    log.info("score1: {}", score1);

    List<String> zrevrangeFriends = jedis.zrevrange("user:123:friendly", 0, 2); // 오름차순
    List<String> zrangeFriends = jedis.zrange("user:123:friendly", 0, 2); // 내림차순
    log.info("friends: {}, {}", zrevrangeFriends, zrangeFriends);
  }

}
