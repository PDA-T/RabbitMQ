package com.rabbitmq.rabbitmq.answer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 生产者
 * 手动应答,防止消息丢失
 */
public class Task2 {
	// 队列名称
	public static final String TASK_QUEUE_NAME = "ack_queue";

	public static void main(String[] args) throws Exception {
		// 信道
		Channel channel = RabbitMqUtils.getChannel();
		// 开启发布确认
		channel.confirmSelect();
		// 声明列队
		channel.queueDeclare(TASK_QUEUE_NAME,false,false,false,null);
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()){
			String massage = scanner.next();
			// 发送消息
			channel.basicPublish("",TASK_QUEUE_NAME,null,massage.getBytes());
			System.out.println("消息发送成功:" + massage);
		}
	}
}
