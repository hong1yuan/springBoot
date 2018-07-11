package com.spring.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/interceptor")
public class InterceptorController {

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    /**
     * 非拦截请求
     */
    @RequestMapping(value = "/normalRequest")
    public String  normalRequest(){
        logger.info("正常请求成功");
        return "正常请求成功";
    }

    /**
     * interceptor请求被拦截
     * 拦截类  URLInterceptor MyWebAppConfigurer
     * http://127.0.0.1:21000/interceptor/interceptorRequest?aa=js&bb=2&cc=3
     */
    @RequestMapping(value = "/interceptorRequest")
    public String  interceptor(){
        logger.info("interceptor拦截失败");
        return "interceptor拦截失败";
    }
}
