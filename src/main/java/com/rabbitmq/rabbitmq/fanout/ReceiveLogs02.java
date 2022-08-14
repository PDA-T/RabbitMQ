package com.rabbitmq.rabbitmq.fanout;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

/**
 * 发布/订阅
 * 消息接收
 */
public class ReceiveLogs02 {
	// 交换机名称
	public static final String EXCHANGE_NAME = "logs";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		// 声明交换机,名字,类型
		channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
		// 临时队列,断开连接自动删除
		String queueName = channel.queueDeclare().getQueue();
		/*
		 * 绑定交换机
		 * 队列名
		 * 交换机名
		 * key
		 */
		channel.queueBind(queueName,EXCHANGE_NAME,"");
		System.out.println("02等待接收消息");
		// 消费者未成功消费消息回调
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			System.out.println("02接收消息:" + new String(message.getBody()));
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
		channel.basicConsume(queueName,true,deliverCallback,cancelCallback);
	}
}
