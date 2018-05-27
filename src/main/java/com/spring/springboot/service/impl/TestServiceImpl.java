package com.spring.springboot.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.executor.ExecutorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.springboot.mapper.TestMapper;
import com.spring.springboot.service.TestService;
@Service
public class TestServiceImpl implements TestService {
	@Autowired
	private TestMapper testMapper;

	@Override
	public List<Map<String, Object>> query(String id) throws Exception{
		return testMapper.query(id);
	}

	public void add(Map<String,Object> param)throws Exception{
		 testMapper.add(param);
	}

}
