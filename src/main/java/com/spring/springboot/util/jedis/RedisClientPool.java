package com.spring.springboot.util.jedis;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class RedisClientPool {
	
	private static Logger log = LoggerFactory.getLogger(RedisClientPool.class);
	
	private static ShardedJedisPool jedisPool = null;
	
	static {
		RedisConfig config = null;
		try{
			log.info("开始从全局配置文件中加载redis配置信息");
			config = SpringContextHolder.getBean("redisConfig");
			init(config);
			log.info("全局配置文件中加载redis配置信息执行完成");
		} catch(Exception e){
			log.error("全局配置文件中的redis配置信息有误，系统尝试从本地配置文件读取redis配置信息。");
			try{
				log.info("开始从本地配置文件中加载redis配置信息");
				InputStream is = RedisClientPool.class.getResourceAsStream("/config.properties");
				Properties prop = new Properties();
                prop.load(is);
				init(prop);
				log.info("本地配置文件中加载redis配置信息执行完成");
			} catch(Exception e1){
				log.error("无法获取本地配置文件中的redis连接信息，请采用指定配置文件的方式进行redis连接。");
			}
		}
	}
	
	/**
	 * 初始化jedis连接池
	 */
	public static void init(RedisConfig config) {
		if(config != null){
			log.info("初始化redis连接池，redis连接配置信息：" + config.toString());
			JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
			// #最大分配的对象数 :控制一个pool最多有多少个状态为idle的jedis实例
			jedisPoolConfig.setMaxTotal(config.getMaxTotal());
			// 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取
			// 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)
			jedisPoolConfig.setMaxIdle(config.getMaxIdle());
			// 当池内没有返回对象时，最大等待时间 : 超时时间
			jedisPoolConfig.setMaxWaitMillis(config.getMaxWaitMillis());
			// 当调用borrow Object方法时，是否进行有效性检查:
			// 在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
			jedisPoolConfig.setTestOnBorrow(true);
			// 当调用return Object方法时，是否进行有效性检查 :在还给pool时，是否提前进行validate操作
			jedisPoolConfig.setTestOnReturn(true);
			
			List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
			String[] nodes = config.getNodes().split(",");
			for (String node : nodes) {
				shards.add(new JedisShardInfo(node.split(":")[0], Integer.valueOf(node.split(":")[1])));
			}
			jedisPool = new ShardedJedisPool(jedisPoolConfig, shards);
	        log.info("初始化redis连接池成功");
		}else{
			log.info("redis配置信息为空，初始化reids连接池失败");
		}
	}
	
	public static void init(Properties prop) {
		RedisConfig config = getLocalRedisConfig(prop);
		init(config);
	}
	
	private static RedisConfig getLocalRedisConfig(Properties prop) {
		RedisConfig config = null;
		try{
			final String KEY_NODES = "redis.nodes";
			final String KEY_TIMEOUT = "redis.timeout";
			final String KEY_POOL_MAXTOTAL = "redis.pool.maxTotal";
			final String KEY_POOL_MAXIDLE = "redis.pool.maxIdle";
			final String KEY_POOL_MAXWAITMILLIS = "redis.pool.maxWaitMillis";
			if(prop.containsKey(KEY_NODES) && !prop.getProperty(KEY_NODES).equals("")){
				//获取redis服务器的ip和port
				String nodes = prop.getProperty(KEY_NODES).trim();
				//获取配置的redis连接超时时间
				int timeout = getIntValueFromProperties(KEY_TIMEOUT, 1000, prop);
				int maxTotal = getIntValueFromProperties(KEY_POOL_MAXTOTAL, 1000, prop);
				int maxIdle = getIntValueFromProperties(KEY_POOL_MAXIDLE, 1000, prop);
				int maxWaitMillis = getIntValueFromProperties(KEY_POOL_MAXWAITMILLIS, 1000, prop);
				config = new RedisConfig(nodes, maxTotal, maxIdle, maxWaitMillis, timeout);
			}else{
				log.error("redis服务器的ip和port配置信息有误。");
			}
		} catch(Exception e){
			log.error("redis连接的文件配置信息不正确", e);
		}
		return config;
	}
	
	private static int getIntValueFromProperties(String key, int defaultValue, Properties prop){
		int value = defaultValue;
		if(prop.containsKey(key) && !prop.getProperty(key).equals("")){
			String valueStr = prop.getProperty(key).trim();
			if(StringUtils.isNumeric(valueStr)){
				value = Integer.parseInt(valueStr);
			}
		}
		return value;
	}
	
	public static ShardedJedisPool getJedisPool(){
		return jedisPool;
	}
}
