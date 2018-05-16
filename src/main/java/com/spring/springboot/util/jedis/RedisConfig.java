package com.spring.springboot.util.jedis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("redisConfig")
public class RedisConfig {

	@Value("${redis.nodes}")
	private String nodes;
	
	@Value("${redis.pool.maxTotal:1000}")
	private int maxTotal;
	
	@Value("${redis.pool.maxIdle:1000}")
	private int maxIdle;
	
	@Value("${redis.pool.maxWaitMillis:1000}")
	private int maxWaitMillis;
	
	@Value("${redis.timeout:1000}")
	private int timeout;

	public RedisConfig() {
		super();
	}
	
	public RedisConfig(String nodes, int maxTotal, int maxIdle, int maxWaitMillis, int timeout) {
		super();
		this.nodes = nodes;
		this.maxTotal = maxTotal;
		this.maxIdle = maxIdle;
		this.maxWaitMillis = maxWaitMillis;
		this.timeout = timeout;
	}

	public String getNodes() {
		return nodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(int maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public String toString() {
		return "RedisConfig [nodes=" + nodes + ", maxTotal=" + maxTotal + ", maxIdle=" + maxIdle + ", maxWaitMillis="
				+ maxWaitMillis + ", timeout=" + timeout + "]";
	}

}
