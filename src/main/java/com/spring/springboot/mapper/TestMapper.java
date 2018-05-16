package com.spring.springboot.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface TestMapper {
	
	List<Map<String, Object>> query(String appid);

}
