package com.spring.springboot.util.jedis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Response;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisCache {

	private static Logger log = LoggerFactory.getLogger(RedisCache.class);

	public static void main(String[] args) {
//		System.out.println(RedisCache.get("a"));
//		TappInfo TappInfo = new TappInfo();
//		TappInfo.setAppid("123");
//		TappInfo.setAppkey("12345");
//		RedisCache.setObject(PushCenterConstant.T_APP_INFO_KEY_PREFIX+"123", TappInfo);
//		Map<String,String> map = new HashMap<String,String>();
//		map.put("imie_appid", "channelCode_device_token");
//		map.put("user_account_appid", "imie");
//		RedisCache.hmset("123",map, 2592000);
//		Map<String,String> map2 = new HashMap<String,String>();
//		map2.put("imie_appid", "channelCode_device_token");
//		map2.put("user_account_appid", "imie");
//		RedisCache.hmset("123",map2, 2592000);
		RedisCache.del("uai18800088163APP1");
		String [] tt ={"1","2"};
		RedisCache.rpush("tttttt", tt, 1800);
		
		String string = RedisCache.get("uai18800088163APP1");
		System.out.println(string);
	}
	/**
	 * 字符串增加
	 * 
	 * @param key reids—key
	 * @param value redis-value
	 * @param value redis过期时间
	 */
	public static void set(String key, String value, int seconds) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			value = RedisUtil.isNullOrEmpty(value) ? "" : value;
			redisClient.set(key, value);
			redisClient.expire(key, seconds);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	/**
	 * 字符串增加
	 * 
	 * @param key
	 * @param value
	 */
	public static void set(String key, String value) {
		set(key, value, 7200);
	}
	
	/**
	 * 对象增加
	 * 
	 * @param key
	 * @param value
	 */
	public static void setObject(String key, Object value) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			if(RedisUtil.isNullOrEmpty(value)){
				return;
			}
			redisClient.set(key.getBytes(), RedisUtil.serialize(value));
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}

	/**
	 * 获取String值
	 * 
	 * @param key
	 * @return value
	 */
	public static String get(String key) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			return redisClient.get(key);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return null;
	}
	
	/**
	 * 对象获取
	 * 
	 * @param key
	 * @return value
	 */
	public static Object getObject(String key) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			byte[] bytes = redisClient.get(key.getBytes());
			if(bytes != null){
				return RedisUtil.unserialize(bytes);
			}
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param flag
	 * @param fields
	 * 
	 */
	public static List<String> hmget(String flag, String... fields) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			return redisClient.hmget(flag, fields);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return null;
	}
	
	/**
	 * 异步读取
	 * @param keys
	 * @param fields
	 * 
	 */
	public static Map<String,Object> hmgetAsync(List<String> keys, String... fields) {
		ShardedJedis redisClient = null;		
		try {						
			// 返回的结果，包括正确的keys的value的集合；和不存在的keys的集合
	        Map<String, Object> result = new HashMap<String, Object>(16);

	        // 正确的keys的value的集合
	        Map<String, List<String>> existResult = new HashMap<String, List<String>>(16);

	        // 错误的key的集合
	        Set<String> errorKeys = new HashSet<String>(16);
			
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			ShardedJedisPipeline pipeline = redisClient.pipelined();
			for (String key : keys) {
				pipeline.hmget(key, fields);
			}
            List<Object> results = pipeline.syncAndReturnAll();
            for (int index = 0; index < results.size(); index++) {
            	if (results.get(index) == null) {
                    errorKeys.add(keys.get(index));
                } else {
                    existResult.put(keys.get(index), (List<String>)results.get(index));
                }            
            }
            
            result.put("error", errorKeys);
            result.put("exist", existResult);
			return result;
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return null;
	}

	/**
	 * 保存数据 类型为 Map
	 * 
	 * @param flag
	 * @param mapData
	 */
	public static void hmset(String flag, Map<String, String> mapData, int seconds) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.hmset(flag, mapData);
			redisClient.expire(flag, seconds);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}

	/**
	 * 保存数据 类型为 key-value
	 * 
	 * @param flag
	 * @param field
	 * @param value
	 */
	public static void hset(String flag, String field, String value) {
		hset(flag, field, value, 1800);
	}
	
	/**
	 * 保存数据 类型为 key-value
	 * 
	 * @param flag
	 * @param field
	 * @param value
	 */
	public static void hset(String flag, String field, String value, int seconds) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.hset(flag, field, value);
			redisClient.expire(flag, seconds);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}

	/**
	 * 获取Map数据
	 * 
	 * @param key
	 * @return
	 */
	public static Map<String, String> hgetAll(String key) {
		Map<String, String> dataMap = null;

		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			dataMap = redisClient.hgetAll(key);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return dataMap;
	}
	
	/**
	 * 获取数据
	 * 
	 * @param keyPrefix
	 * @return
	 */
	public Map<String, Response<List<String>>> getMutilMapData(String keyPrefix, Map<String, String[]> keys) {
		if(keyPrefix != null){
			keyPrefix = keyPrefix.trim();
		}else{
			keyPrefix = "";
		}
		if(null == keys || keys.isEmpty()){
			return null;
		}
		Map<String, Response<List<String>>> responseMap = new HashMap<>();
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			ShardedJedisPipeline pipeline = redisClient.pipelined();
			for(Map.Entry<String, String[]> e : keys.entrySet()){
				responseMap.put(e.getKey(), pipeline.hmget(keyPrefix + e.getKey(), e.getValue()));
			}
			pipeline.sync();
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return responseMap;
	}

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return 0;
			}
			redisClient = jedisPool.getResource();
			result = redisClient.del(key);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return result;
	}

	/**
	 * 根据key和字段获取数据
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public static String hget(String key, String value) {
		String data = null;
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			data = redisClient.hget(key, value);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return data;
	}
	
	public static void lpush(String key, String value) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.lpush(key, value);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static void lpush(String key, String value, int seconds) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.expire(key, seconds);
			redisClient.lpush(key, value);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static void lpush(String key, String[] value) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.lpush(key, value);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static void lpush(String key, String[] value, int seconds) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.expire(key, seconds);
			redisClient.lpush(key, value);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static void rpush(String key, String value) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.rpush(key, value);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static void rpush(String key, String value, int seconds) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.rpush(key, value);
			redisClient.expire(key, seconds);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static void rpush(String key, String[] value) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.rpush(key, value);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static void rpush(String key, String[] value, int seconds) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return;
			}
			redisClient = jedisPool.getResource();
			redisClient.rpush(key, value);
			redisClient.expire(key, seconds);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
	}
	
	public static List<String> lrange(String key) {
		return lrange(key, 0, -1);
	}
	
	public static List<String> lrange(String key, long start, long end) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			return redisClient.lrange(key, start, end);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return null;
	}
	
	public static String rpop(String key) {
		ShardedJedis redisClient = null;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return null;
			}
			redisClient = jedisPool.getResource();
			return redisClient.rpop(key);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return null;
	}
	
	public static Long hlen(String key){
		ShardedJedis redisClient = null;
		Long len = 0l;
		try {
			ShardedJedisPool jedisPool = RedisClientPool.getJedisPool();
			if(null == jedisPool){
				log.warn("操作失败，无法获取redis连接。");
				return len;
			}
			redisClient = jedisPool.getResource();
			len = redisClient.hlen(key);
		} catch (Exception e) {
			log.error("", e);
		} finally {
			if (redisClient != null) {
				redisClient.close();
			}
		}
		return len;
	}
}
