package com.rabbitmq.rabbitmq.primary;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 生产者
 */
public class Producer {
	// 队列名称
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception{
		// 连接工厂
		ConnectionFactory factory = new ConnectionFactory();
		// ip
		factory.setHost("43.142.191.46");
		// 用户名
		factory.setUsername("user_proj");
		// 密码
		factory.setPassword("123456");

		// 创建连接
		Connection connection = factory.newConnection();
		// 获取信道
		Channel channel = connection.createChannel();
		/*
		 * 生成队列
		 * 队列名
		 * 是否持久化
		 * 是否允许多个消费者共享消息
		 * 是否自动删除
		 * 其他参数
		 */
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		// 消息内容
		String message = "rabbitmq message test";
		/*
		 * 发送消息
		 * 发送到某个交换机
		 * 路由的key值,队列名称
		 * 其他参数
		 * 消息内容
		 */
		channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
		System.out.println("消息发送完毕");
	}
}
