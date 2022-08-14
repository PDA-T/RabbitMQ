package com.rabbitmq.rabbitmq.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.Scanner;

/**
 * 直接交换机
 * 发送消息
 */
public class DirectLogs {
	// 交换机名称
	public static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNext()){
			String message = scanner.next();
			// 发送消息,交换机key为空串
			channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes());
			System.out.println("消息发送完毕:" + message);
		}
	}
}
