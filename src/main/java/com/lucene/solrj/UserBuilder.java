package com.lucene.solrj;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.time.DateUtils;

import redis.clients.jedis.Jedis;

public class UserBuilder {

	private UserBuilder() {
	}

	private static Jedis jedis;

	static {
		jedis = new Jedis("192.168.1.58", 6379);
		jedis.auth("1234");
	}

	public static List<User> getBuilderUsers(int count) {
		List<User> users = new ArrayList<User>();
		for (int i = 1; i <= count; i++) {
			User user = new User();
			user.setId(getNextId());
			user.setName(buildName());
			user.setAge(buildAge());
			user.setAddress(buildAddress());
			user.setCreateAt(buildCreateAt());
			users.add(user);
		}
		jedis.close();
		return users;
	}

	private static String getNextId() {
		long nextId = jedis.incr("solr:auto:id");
		return String.valueOf(nextId);
	}

	private static String buildName() {
		return UUID.randomUUID().toString().split("-")[0];
	}

	private static int buildAge() {
		return RandomUtils.nextInt(1, 100);
	}

	private static String buildAddress() {
		String[] adds = { "shanghai", "beijing", "guangzhou", "nanjing", "xizang", "xianggang", "taiwan", "aomen ",
				"hunan", "hubei", "heinan", "shenzhen", "yunnan", "heilongjiang" };
		return adds[RandomUtils.nextInt(0, adds.length - 1)];
	}

	private static Date buildCreateAt() {
		Date now = new Date();
		now = DateUtils.addDays(now, -RandomUtils.nextInt(1, 1000));
		return now;
	}
}
