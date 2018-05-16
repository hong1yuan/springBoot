package com.spring.springboot.service;

import java.util.concurrent.Future;


public interface Multithreading {
	//普通类
	String multithreading(Integer i) throws Exception;
		
	//多线程 异步
	Future<Long> multithreadingAsyn(Integer i) throws Exception;
	
}
