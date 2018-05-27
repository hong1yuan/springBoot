package com.spring.springboot.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.spring.springboot.service.TestService;

@RestController
@RequestMapping(value = "/testConfig")
public class TestConfigController {
	
	private static final Logger fileLog = LoggerFactory.getLogger("springBootDiy");
	
	@Value("${ftp}")//读取配置文件 ftp自定义属性
	private String ftp;
	
	@Autowired
	TestService testService;
	
	//读取配置文件 ftp自定义属性
	@RequestMapping(value = "/getConfig")
	public void getConfig(){
		System.out.println(ftp);
		
	}
	
	//查询语句
	//http://127.0.0.1:21000/testConfig/query?id=1
	@RequestMapping(value = "/query")
	public String query(@RequestParam("id") String id)throws Exception{
		List<Map<String, Object>> query = testService.query(id);
		String jsonString = JSON.toJSONString(query);
		return jsonString;
	}

	//添加语句
	//http://127.0.0.1:21000/testConfig/add
	@RequestMapping(value = "/add")
	public void add()throws Exception{
		Map<String,Object> param =  new HashMap<String, Object>();
		param.put("name","李四");
		param.put("age","27");

		testService.add(param);
	}
	
	//打印日志到单独一个文件
	//http://127.0.0.1:21000/testConfig/log
	@RequestMapping(value = "log")
	public void log(){
		fileLog.info("成功将日志打印到文件中");
	}

}
