package com.rabbitmq.rabbitmq.deadmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列
 * 消费者
 */
public class Consumer01 {
	// 交换机
	public static final String NORMAL_EXCHANGE = "normal_exchange";
	// 死信交换机
	public static final String DEAD_EXCHANGE = "dead_exchange";
	// 列队
	public static final String NORMAL_QUEUE = "normal_queue";
	// 死信列队
	public static final String DEAD_QUEUE = "dead_queue";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		// 声明交换机
		channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
		channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
		// 转发参数
		Map<String, Object> arguments = new HashMap<String,Object>();
		// 过期时间,发消息时指定
		// arguments.put("x-massage-ttl",100000);
		// 死信交换机转发
		arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
		// 设置死信Key
		arguments.put("x-dead-letter-routing-key","lisi");
		// 设置队列长度
		// arguments.put("x-max-length",6);

		// 声明队列
		channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
		// 死信列队
		channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

		// 绑定队列
		channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
		channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
		System.out.println("01等待接收消息");

		// 消费者未成功消费消息回调
		DeliverCallback deliverCallback = (consumerTag, message) -> {
			// 获取消息
			String msg = new String(message.getBody());
			if (msg.equals("info5")){
				// 拒绝消息,不放回队列
				channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
				System.out.println("01拒绝消息:" + msg);
			}else {
				// 确认应答,不批量应答
				channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
				System.out.println("01接收消息:" + new String(message.getBody()));
			}
		};
		// 取消消息时回调声明
		CancelCallback cancelCallback = (consumerTag) -> {
			System.out.println("消息中断");
		};
		// 开启手动应答
		channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);
	}
}
