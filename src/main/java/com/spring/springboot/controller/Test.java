package com.spring.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/aa")
public class Test {
	private static final Logger logger = LoggerFactory.getLogger(Test.class);

	@RequestMapping(value = "/test")
	public void  test(){
		logger.info("成功");
	}


}
