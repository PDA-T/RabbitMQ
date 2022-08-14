package com.rabbitmq.springboot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * 发送延迟消息
 * 控制层
 */
@RestController
@Slf4j
@RequestMapping("/ttl")
public class SendMsgController {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 发送定时消息
	 * @param message
	 */
	@GetMapping("/sendmsg/{message}")
	public void sendMsg(@PathVariable String message){
		// 日志
		log.info("当前时间:{},发送一条信息给两个TTL队列:{}",new Date().toString(),message);
		// 发送消息
		rabbitTemplate.convertAndSend("X","XA","消息来自TTL为10s的队列:" + message);
		rabbitTemplate.convertAndSend("X","XB","消息来自TTL为40s的队列:" + message);
	}

	/**
	 * 发送自定义时间消息
	 * @param message 消息
	 * @param ttltime 时间
	 */
	@GetMapping("/sendexpirationmsg/{message}/{ttltime}")
	public void sendMsg(@PathVariable String message,@PathVariable String ttltime){
		// 日志
		log.info("当前时间:{},发送一条时长{}ms给TTL队列QC:{}",new Date().toString(),ttltime,message);
		// 设置消息延迟时间
		MessagePostProcessor postProcessor = (msg) -> {
			msg.getMessageProperties().setExpiration(ttltime);
			return msg;
		};
		// 发送消息
		rabbitTemplate.convertAndSend("X","XC","消息来自TTL为QC的队列:" + message,postProcessor);
	}
}
