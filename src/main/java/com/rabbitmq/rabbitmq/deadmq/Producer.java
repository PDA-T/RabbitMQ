package com.rabbitmq.rabbitmq.deadmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

/**
 * 死信列队
 * 生产者
 */
public class Producer {
	// 交换机
	public static final String NORMAL_EXCHANGE = "normal_exchange";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		// 设置TTL时间
		AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
		for (int i = 0;i<10;i++){
			String message = "info" + i;
			channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
		}
	}
}
