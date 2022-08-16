package com.rabbitmq.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 高级发布确认
 * 备份交换机
 */
@Configuration
public class ConfirmConfig {
	// 交换机
	public static final String CONFIRM_EXCHANGE_NAME = "confirm_exchange";
	// 队列
	public static final String CONFIRM_QUEUE_NAME = "confirm_queue";
	// Key
	public static final String CONFIRM_ROUTING_KEY = "key1";
	// 备份交换机
	public static final String BACKUP_EXCHANGE_NAME = "backup_exchange";
	// 备份队列
	public static final String BACKUP_QUEUE_NAME = "backup_queue";
	// 报警队列
	public static final String WARNING_QUEUE_NAME = "warning_queue";

	/**
	 * 声明交换机
	 * @return
	 */
	@Bean
	public DirectExchange confirmExchange(){
		// 持久化,转发到备份交换机
		return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
				.withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
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

	/**
	 * 声明备份交换机
	 * @return
	 */
	@Bean("backupExchange")
	public FanoutExchange backupExchange(){
		return new FanoutExchange(BACKUP_EXCHANGE_NAME);
	}

	/**
	 * 声明备份队列
	 * @return
	 */
	@Bean("backupQueue")
	public Queue backupQueue(){
		return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
	}

	/**
	 * 声明报警队列
	 * @return
	 */
	@Bean("warningQueue")
	public Queue warningQueue(){
		return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
	}

	/**
	 * 绑定备份交换机
	 * @param backupQueue 备份队列
	 * @param backupExchange 备份交换机
	 * @return
	 */
	@Bean
	public Binding backupQueueBindingBackupExchange(@Qualifier("backupQueue") Queue backupQueue,
										@Qualifier("backupExchange") FanoutExchange backupExchange){
		return BindingBuilder.bind(backupQueue).to(backupExchange);
	}

	/**
	 * 绑定备份交换机
	 * @param warningQueue 报警队列
	 * @param backupExchange 备份交换机
	 * @return
	 */
	@Bean
	public Binding warningQueueBindingBackupExchange(@Qualifier("warningQueue") Queue warningQueue,
													@Qualifier("backupExchange") FanoutExchange backupExchange){
		return BindingBuilder.bind(warningQueue).to(backupExchange);
	}
}
