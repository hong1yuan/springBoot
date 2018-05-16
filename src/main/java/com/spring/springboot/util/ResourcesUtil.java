package com.spring.springboot.util;

import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 获取config.properties文件的配置信息
 * @author wangzhiyong@richinfo.cn
 *
 */
public class ResourcesUtil {

	private static Logger log = LoggerFactory.getLogger(ResourcesUtil.class);
	
	private static ResourceBundle template = ResourceBundle.getBundle("config/application");
	
	public static String getValue(String key){
		return template.getString(key);
	}
	
	public static String getValue(String key, String defaultValue){
		String v = template.getString(key);
		return (v == null || v.trim().equals("")) ? defaultValue : v.trim();
	}
	
	public static int getInt(String key, int defaultValue){
		String value = template.getString(key);
		try{
			return Integer.parseInt(value);
		}catch(Exception e){
			log.error("类型解析错误，无法转换为整数，转换的值为：" + value, e);
			return defaultValue;
		}
	}
}
