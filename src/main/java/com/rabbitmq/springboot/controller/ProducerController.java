package com.rabbitmq.springboot.controller;

import com.rabbitmq.springboot.config.ConfirmConfig;
import com.rabbitmq.springboot.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 高级消息确认
 */
@RestController
@Slf4j
@RequestMapping("/confirm")
public class ProducerController {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 发送消息
	 * @param message
	 */
	@GetMapping("/sendmessage/{message}")
	public void sendMessage(@PathVariable String message){
		// 消息相关内容,回调时显示
		CorrelationData correlationData = new CorrelationData("1");
		// 日志
		log.info("当前时间:{},发送一条信息给确认列队:{}",new Date().toString(),message);
		// 发送消息
		rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
				ConfirmConfig.CONFIRM_ROUTING_KEY,"消息:" + message,correlationData);


		/*
		 * 错误key或者错误交换机
		 * 回退处理
		 */
		CorrelationData correlationData2 = new CorrelationData("2");
		// 日志
		log.info("当前时间:{},发送一条信息给确认列队:{}",new Date().toString(),message);
		// 发送消息
		rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME,
				ConfirmConfig.CONFIRM_ROUTING_KEY + "2","消息:" + message,correlationData2);
	}
}
