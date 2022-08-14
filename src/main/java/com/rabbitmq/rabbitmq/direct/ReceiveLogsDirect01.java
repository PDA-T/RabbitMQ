package com.rabbitmq.rabbitmq.direct;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

/**
 * 直接交换机
 * 消息接收
 */
public class ReceiveLogsDirect01 {
	// 交换机名称
	public static final String EXCHANGE_NAME = "direct_logs";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		// 声明交换机,名字,类型
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
		// 队列
		channel.queueDeclare("console",false,false,false,null);
		/*
		 * 绑定交换机
		 * 队列名
		 * 交换机名
		 * key
		 */
		channel.queueBind("console",EXCHANGE_NAME,"info");
		// 绑定多个
		channel.queueBind("console",EXCHANGE_NAME,"warning");
		System.out.println("01等待接收消息");
		// 消费者未成功消费消息回调
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("01接收消息:" + new String(message.getBody()));
		};
		// 取消消息时回调声明
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("消息中断");
		};
		/**
		 * 消费消息
		 * 消费队列名
		 * 应答方式
		 * 消费者未成功消费消息回调
		 * 消费者取消消费的回调
		 */
		channel.basicConsume("console",true,deliverCallback,cancelCallback);
	}
}
