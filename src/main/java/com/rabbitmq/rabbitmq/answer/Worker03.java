package com.rabbitmq.rabbitmq.answer;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;
import com.rabbitmq.rabbitmq.utils.SleepUtils;

/**
 * 消费者
 * 手动应答,防止消息丢失
 */
public class Worker03 {
	// 队列名称
	public static final String TASK_QUEUE_NAME = "ack_queue";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		System.out.println("C1等待接收消息");
		// 接收消息时回调声明
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			SleepUtils.sleep(1);
			// 打印接收到的消息
			System.out.println("接收到的消息:" + new String(message.getBody()));
			/*
			 * 手动应答
			 * 消息的标记 tag
			 * 是否批量应答
			 */
			channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
		};
		// 取消消息时回调声明
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("消息中断");
		};
		// 设置不公平分发
		// channel.basicQos(1);
		// 设置预取值2
		channel.basicQos(2);
		// 手动应答
		boolean autoAck = false;
		channel.basicConsume(TASK_QUEUE_NAME,autoAck,deliverCallback,cancelCallback);
	}
}
