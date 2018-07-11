package com.spring.springboot.springBoot;

import com.alibaba.fastjson.JSON;
import com.spring.springboot.service.TestService;
import com.spring.springboot.springBootApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = springBootApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AutowiredTest {
    @Autowired
    TestService testService;
    @Test
    public void aa()throws Exception{
        List<Map<String, Object>> query = testService.query("103");
        String jsonString = JSON.toJSONString(query);
        System.out.println(jsonString);

    }
}
