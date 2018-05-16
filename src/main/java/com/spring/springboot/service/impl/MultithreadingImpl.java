package com.spring.springboot.service.impl;

import java.util.concurrent.Future;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;




import com.spring.springboot.service.Multithreading;
@Service
public class MultithreadingImpl implements Multithreading {
	
	@Override
	public String multithreading(Integer i) throws Exception {
		System.out.println("执行普通任务+1：" + (i+1));
		return "普通类";
	}

	@Override
	@Async("filterUsersetExecutor")
	public Future<Long> multithreadingAsyn(Integer i) throws Exception {
		System.out.println("执行异步任务+1：" + (i+1));
		return (Future<Long>) new AsyncResult<>(0L);
	}

}
