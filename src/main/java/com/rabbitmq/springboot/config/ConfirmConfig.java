package com.rabbitmq.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 高级发布确认
 */
@Configuration
public class ConfirmConfig {
	// 交换机
	public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
	// 队列
	public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
	// Key
	public static final String CONFIRM_ROUTING_KEY = "key1";

	/**
	 * 声明交换机
	 * @return
	 */
	@Bean
	public DirectExchange confirmExchange(){
		return new DirectExchange(CONFIRM_EXCHANGE_NAME);
	}

	/**
	 * 声明队列
	 * @return
	 */
	@Bean
	public Queue confirmQueue(){
		return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
	}

	/**
	 * 绑定交换机
	 * @param confirmQueue
	 * @param confirmExchange
	 * @return
	 */
	@Bean
	public Binding queueBindingExchange(@Qualifier("confirmQueue") Queue confirmQueue,
										 @Qualifier("confirmExchange") DirectExchange confirmExchange){
		return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
	}
}
