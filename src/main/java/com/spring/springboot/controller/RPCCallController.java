package com.spring.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value = "/rpc")
public class RPCCallController {
    @Autowired
    private RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    /**
     * rpc调用
     */
    @RequestMapping(value = "/call")
    public  List<String>  call(){
        logger.info("rpc成功");
        List<String> rpc = this.restTemplate.getForObject("http://127.0.0.1:27070/bb/test", List.class);
        logger.info("rpc返回结果"+rpc);
        return rpc;
    }

}
