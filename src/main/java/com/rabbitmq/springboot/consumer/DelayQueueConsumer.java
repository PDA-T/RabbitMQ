package com.rabbitmq.springboot.consumer;

import com.rabbitmq.springboot.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 基于插件的延迟消息
 */
@Component
@Slf4j
public class DelayQueueConsumer {

	/**
	 * 监听消息
	 * @param message
	 */
	@RabbitListener(queues = DelayedQueueConfig.DELAYED_QUEUE_NAME)
	public void receiveDelayQueue(Message message){
		String msg = new String(message.getBody());
		log.info("当前时间:{},收到延迟列队消息:{}",new Date().toString(),msg);
	}
}
