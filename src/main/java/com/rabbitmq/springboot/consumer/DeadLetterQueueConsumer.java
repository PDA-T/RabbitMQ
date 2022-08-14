package com.rabbitmq.springboot.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 队列TTL
 * 消费者
 */
@Slf4j
@Component
public class DeadLetterQueueConsumer {
	/**
	 * 接收消息
	 * @param massage
	 * @param channel
	 * @throws Exception
	 */
	@RabbitListener(queues = "QD")
	public void receiveD(Message massage, Channel channel) throws Exception{
		// 消息
		String msg = new String(massage.getBody());
		// 日志
		log.info("当前时间:{},收到死信队列的消息:{}",new Date().toString(),msg);
	}
}
