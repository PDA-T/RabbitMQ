package com.rabbitmq.rabbitmq.fanout;

import com.rabbitmq.client.Channel;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 发布/订阅
 * 发布消息
 */
public class EmitLog {
	// 交换机名称
	public static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()){
			String message = scanner.next();
			// 发送消息,交换机key为空串
			channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
			System.out.println("消息发送完毕:" + message);
		}
	}
}
