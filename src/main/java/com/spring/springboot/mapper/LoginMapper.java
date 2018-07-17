package com.spring.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


public interface LoginMapper {

	Map<String, Object> query(Map<String,Object> param) throws Exception;

}
