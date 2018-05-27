package com.spring.springboot.service;

import java.util.List;
import java.util.Map;

public interface TestService {
	
	List<Map<String, Object>> query(String id) throws Exception;

	void add(Map<String,Object> param) throws Exception;

}
