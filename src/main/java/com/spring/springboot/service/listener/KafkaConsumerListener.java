package com.spring.springboot.service.listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
//import org.richinfo.mc.userset.service.DataDeal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {
	@Autowired
	//DataDeal dataDeal;
	
	private static Logger log = LoggerFactory.getLogger(KafkaConsumerListener.class);
	
	@KafkaListener(topics = { "springBootKafka" }, group = "myGroup")
	public void processMessage(ConsumerRecord<String, String> record){
		String row = record.value();
		log.info("kafkaData:"+row);
		log.info(record.toString());
		//dataDeal.dealUserDataFtpAsync(row);
		
	}

}
