package com.rabbitmq.rabbitmq.worker;

import com.rabbitmq.client.Channel;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 生产者01
 */
public class Task01 {
	// 队列名称
	public static final String QUEUE_NAME = "hello";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		/*
		 * 生成队列
		 * 队列名
		 * 是否持久化
		 * 是否允许多个消费者共享消息
		 * 是否自动删除
		 * 其他参数
		 */
		channel.queueDeclare(QUEUE_NAME,false,false,false,null);
		// 接收控制台消息
		Scanner scanner = new Scanner(System.in);
		// 是否还有消息
		while (scanner.hasNext()){
			// 消息内容
			String message = scanner.next();
			/*
			 * 发送消息
			 * 发送到某个交换机
			 * 路由的key值,队列名称
			 * 其他参数
			 * 消息内容
			 */
			channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
			System.out.println("发送消息完成:" + message);
		}
	}
}
