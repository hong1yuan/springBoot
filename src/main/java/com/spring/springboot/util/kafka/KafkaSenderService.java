package com.spring.springboot.util.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFuture;

/**
 * kafka生产消息接口
 *
 * @author LiLin
 */
@Component
public class KafkaSenderService {
	private static final Logger log = LoggerFactory.getLogger(KafkaSenderService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 同步发送消息
     */
    public void syncSendMessage(String topic, String data) {
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate
            .send(topic, data);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			public void onSuccess(SendResult<String, String> result) {
				log.info("syncsendmessage success record:{}",
	                    result.getProducerRecord().toString());
			}

			public void onFailure(Throwable ex) {
				log.error("syncsendmessage error", ex);
			}
        	
        });
    }
    
    public void syncSendMessage(String topic, String key, String data) {
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate
            .send(topic, key, data);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
			public void onSuccess(SendResult<String, String> result) {
				log.info("syncsendmessage success record:{}",
	                    result.getProducerRecord().toString());
			}

			public void onFailure(Throwable ex) {
				log.error("syncsendmessage error", ex);
			}
        	
        });
    }

}
