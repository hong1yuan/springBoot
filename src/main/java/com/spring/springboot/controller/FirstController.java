package com.spring.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/")
public class FirstController {

    @RequestMapping(value = "/ok")
    public String ok(){
        return "ok";
    }
}
