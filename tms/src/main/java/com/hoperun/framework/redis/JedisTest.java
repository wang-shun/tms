package com.hoperun.framework.redis;

import redis.clients.jedis.Jedis;

public class JedisTest {
	public static void main(String[] args) {
		// 操作单独的文本串
		Jedis redis = new Jedis("120.26.206.23", 6379);
		redis.auth("shinho555666777888999");
		redis.set("key", "value");
		System.out.println(redis.get("key"));
		System.out.println(redis.del("key"));

		User user = new User(); // 这个Goods实体我就不写了啊
		user.setId("mjb1");
		user.setName("redis");
		user.setPassword("123");
		redis.set("mjb.1".getBytes(), SerializeUtil.serialize(user));
		byte[] value = redis.get("mjb.1".getBytes());
		Object object = SerializeUtil.unserialize(value);
		if (object != null) {
			User user_1 = (User) object;
			System.out.println(user_1.getId());
			System.out.println(user_1.getName());
			System.out.println(user_1.getPassword());
		}
		System.out.println(redis.del("good".getBytes()));
	}
}
