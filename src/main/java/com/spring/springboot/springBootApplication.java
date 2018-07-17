package com.spring.springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
//@EnableAutoConfiguration
//@ComponentScan(basePackages={"com.spring.springboot"})
//扫描dao层 有@MapperScan(注解则不需要在mapper接口中写@Mapper注解（启动类中写@MapperScan和mapper接口中写@Mapper注解存在一个就行）
@MapperScan(basePackages= {"com.spring.springboot.mapper"})
@EnableScheduling	//增加支持定时任务的注解
@EnableAsync		//多线程
public class springBootApplication {

	/*@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}*/

	public static void main(String[] args) {
		SpringApplication.run(springBootApplication.class, args);
	}

}
