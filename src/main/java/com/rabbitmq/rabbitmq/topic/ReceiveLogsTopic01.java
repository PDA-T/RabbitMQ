package com.rabbitmq.rabbitmq.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

/**
 * 主题模式
 * 消费者
 */
public class ReceiveLogsTopic01 {
	// 交换机名称
	public static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		// 声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
		// 队列名称
		String queueName = "Q1";
		channel.queueDeclare(queueName,false,false,false,null);
		// 绑定
		channel.queueBind(queueName,EXCHANGE_NAME,"*.orange.*");
		System.out.println("01等待接收消息");
		// 消费者未成功消费消息回调
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("01接收消息:" + new String(message.getBody()));
			System.out.println("绑定键:" + message.getEnvelope().getRoutingKey());
		};
		// 取消消息时回调声明
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("消息中断");
		};
		// 接收消息
		channel.basicConsume(queueName,true,deliverCallback,cancelCallback);
	}
}
