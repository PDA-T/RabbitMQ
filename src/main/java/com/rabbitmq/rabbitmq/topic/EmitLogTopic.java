package com.rabbitmq.rabbitmq.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 主题模式
 * 生产者
 */
public class EmitLogTopic {
	// 交换机名称
	public static final String EXCHANGE_NAME = "topic_logs";

	public static void main(String[] args) throws Exception {
		Channel channel = RabbitMqUtils.getChannel();
		Map<String,String> bindingKeyMap = new HashMap<String,String>();
		bindingKeyMap.put("quick.orange.rabbit","Q1Q2");
		bindingKeyMap.put("lazy.orange.elephant","Q1Q2");
		bindingKeyMap.put("quick.orange.fox","Q1");
		bindingKeyMap.put("lazy.brown.fox","Q2");
		for (Map.Entry<String, String> bindingKeyEntry : bindingKeyMap.entrySet()) {
			String routingKey = bindingKeyEntry.getKey();
			String message = bindingKeyEntry.getValue();
			channel.basicPublish(EXCHANGE_NAME,routingKey,null,message.getBytes());
			System.out.println("发送完毕:" + message);
		}
	}
}
