package com.spring.springboot.controller;

import com.alibaba.fastjson.JSON;
import com.spring.springboot.service.LoginService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {
    @Autowired
    LoginService loginService;

    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    @RequestMapping(value = "/userLogin")
    public Map<String, Object> userLogin(String username,String password)throws Exception{
        logger.info("进入登录方法");
        //用户名和密码等于空
        if(StringUtils.isBlank(username) && StringUtils.isBlank(password)){
            HashMap<String, Object> failure = new HashMap<String, Object>();
            failure.put("succeed","false");
            return failure;
        }
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("username",username);
        param.put("password",password);
        Map<String, Object> result = loginService.query(param);
        if(result == null){
            result = new HashMap<String, Object>();
            result.put("succeed","false");
            return result;
        }
        result.put("succeed","true");
        return result;
    }
}
