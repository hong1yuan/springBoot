package com.spring.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springboot.service.Multithreading;

@RestController
@RequestMapping(value = "/multithreading")
public class MultithreadingController {
	@Autowired
	Multithreading multithreading;
	
	//多线程 异步
	//http://127.0.0.1:21000/multithreading/multithreadingAsyn
	@RequestMapping(value = "/multithreadingAsyn")
	public void multithreadingAsyn() throws Exception{
		for(int i=0;i<100;i++){
			multithreading.multithreadingAsyn(i);
		}
	}
	//单线程
	//http://127.0.0.1:21000/multithreading/thread
	@RequestMapping(value = "/thread")
	public void thread() throws Exception{
		for(int i=0;i<100;i++){
			multithreading.multithreading(i);
		}
	}
	

}
