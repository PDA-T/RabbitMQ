package com.rabbitmq.rabbitmq.worker;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

/**
 * 工作线程2
 */
public class Worker02 {
	// 队列名称
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();

		// 接收消息时回调声明
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			// 打印接收到的消息
			System.out.println("接收到的消息:" + new String(message.getBody()));
		};
		// 取消消息时回调声明
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println(consumerTag + "消息中断");
		};
		System.out.println("C2等待接收消息......");
		/**
		 * 消费消息
		 * 消费队列名
		 * 应答方式
		 * 消费者未成功消费消息回调
		 * 消费者取消消费的回调
		 */
		channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
	}
}
