package com.rabbitmq.rabbitmq.priority;

import com.rabbitmq.client.*;

/**
 * 消息优先级
 */
public class Consumer {
	// 队列名称
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		// 连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("43.142.191.46");
		factory.setUsername("user_proj");
		factory.setPassword("123456");
		// 创建连接
		Connection connection = factory.newConnection();
		// 创建信道
		Channel channel = connection.createChannel();
		// 接收消息时回调声明
		DeliverCallback deliverCallback = (consumerTag,message) -> {
			// 打印接收到的消息
			System.out.println(new String(message.getBody()));
		};
		// 取消消息时回调声明
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("消息中断");
		};

		/*
		 * 消费消息
		 * 消费队列名
		 * 应答方式
		 * 消费者未成功消费消息回调
		 * 消费者取消消费的回调
		 */
		channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
	}
}
