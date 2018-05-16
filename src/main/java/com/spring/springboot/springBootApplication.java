package com.spring.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan(basePackages={"com.spring.springboot"})
@EnableScheduling	//增加支持定时任务的注解
@EnableAsync		//多线程
public class springBootApplication {
	public static void main(String[] args) {
		SpringApplication.run(springBootApplication.class, args);
	}

}
