package com.spring.springboot.mapper;

import java.util.List;
import java.util.Map;


public interface MyDynamicTaskMapper {

	Map<String, Object> query(String id) throws Exception;

}
