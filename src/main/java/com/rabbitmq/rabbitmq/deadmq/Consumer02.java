package com.rabbitmq.rabbitmq.deadmq;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列
 * 消费者
 */
public class Consumer02 {
	public static final String DEAD_QUEUE = "dead_queue";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		// 消费者未成功消费消息回调
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("01接收消息:" + new String(message.getBody()));
		};
		// 取消消息时回调声明
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("消息中断");
		};
		channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
	}
}
