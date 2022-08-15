package com.rabbitmq.springboot.consumer;

import com.rabbitmq.springboot.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 接收高级确认消息
 */
@Component
@Slf4j
public class Consumer {

	/**
	 * 接收消息
	 * @param message
	 */
	@RabbitListener(queues = ConfirmConfig.CONFIRM_QUEUE_NAME)
	public void receiveConfirmMessage(Message message){
		// 消息
		String msg = new String(message.getBody());
		log.info("接收到的队列confirm.queue消息:{}",msg);
	}
}
