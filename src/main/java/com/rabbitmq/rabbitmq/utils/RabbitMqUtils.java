package com.rabbitmq.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 工具类
 */
public class RabbitMqUtils {
	// 得到一个连接的channel
	public static Channel getChannel() throws Exception{
		// 创建连接
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("43.142.191.46");
		factory.setUsername("user_proj");
		factory.setPassword("123456");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		return channel;
	}
}
