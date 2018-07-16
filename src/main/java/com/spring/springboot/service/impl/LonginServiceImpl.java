package com.spring.springboot.service.impl;

import com.spring.springboot.mapper.LoginMapper;
import com.spring.springboot.mapper.TestMapper;
import com.spring.springboot.service.LoginService;
import com.spring.springboot.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LonginServiceImpl implements LoginService {
	@Autowired
	private LoginMapper loginMapper;

	@Override
	public Map<String, Object> query(Map<String,Object> param) throws Exception{
		return loginMapper.query(param);
	}

}
