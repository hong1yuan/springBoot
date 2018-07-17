package com.spring.springboot.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


public interface TestMapper {
	
	List<Map<String, Object>> query(String id) throws Exception;

	void add(Map<String,Object> param) throws Exception;

}
