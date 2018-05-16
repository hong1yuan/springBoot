package com.spring.springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springboot.util.kafka.KafkaSenderService;

@RestController
@RequestMapping(value = "/kafkaTest")
public class KafkaTestController {
	private static final Logger logger  = LoggerFactory.getLogger(KafkaTestController.class);
	@Autowired
	KafkaSenderService kafkaSenderService;
	@RequestMapping(value = "/kafka")
	public void kafka(){
		String topic = "springBootKafka";
		String data = "abc";
		logger.debug("topic:"+topic+"   data:"+data);
		kafkaSenderService.syncSendMessage(topic, data);
	}

}
