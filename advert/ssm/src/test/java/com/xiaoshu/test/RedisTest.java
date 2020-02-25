package com.xiaoshu.test;


import org.junit.Test;
import redis.clients.jedis.Jedis;

public class RedisTest {

    @Test
    public void test01(){
        Jedis jedis=new Jedis("192.168.109.129",6379);
        jedis.set("age","18");
        String age = jedis.get("age");
        System.out.println(age);
    }
}
