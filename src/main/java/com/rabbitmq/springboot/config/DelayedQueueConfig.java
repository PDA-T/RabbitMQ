package com.rabbitmq.springboot.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * 基于插件延迟消息
 */
@Configuration
public class DelayedQueueConfig {
	// 交换机
	public static final String DELAYED_EXCHANGE_NAME = "delayed.exchange";
	// 队列
	public static final String DELAYED_QUEUE_NAME = "delayed.queue";
	// Key
	public static final String DELAYED_ROUTING_KEY = "delayed.routingkey";

	/**
	 * 声明自定义交换机
	 * @return
	 */
	@Bean
	public CustomExchange delayedExchange(){
		Map<String,Object> arguments = new HashMap<String,Object>();
		// 延迟类型(直接类型)
		arguments.put("x-delayed-type","direct");
		/*
		 * 交换机名称
		 * 交换机类型
		 * 是否持久化
		 * 是否自动删除
		 * 参数
		 */
		return new CustomExchange(DELAYED_EXCHANGE_NAME,"x-delayed-message",true,false,arguments);
	}

	/**
	 * 声明队列
	 * @return
	 */
	@Bean
	public Queue delayedQueue(){
		return new Queue(DELAYED_QUEUE_NAME);
	}

	/**
	 * 绑定交换机
	 * @param delayedQueue 延迟列队
	 * @param delayedExchange 延迟交换机
	 * @return
	 */
	@Bean
	public Binding delayedQueueBindingDelayedExchange(@Qualifier("delayedQueue") Queue delayedQueue,
													  @Qualifier("delayedExchange") CustomExchange delayedExchange){
		return BindingBuilder.bind(delayedQueue).to(delayedExchange).with(DELAYED_ROUTING_KEY).noargs();
	}
}
