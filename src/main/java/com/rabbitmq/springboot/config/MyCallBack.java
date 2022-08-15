package com.rabbitmq.springboot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 实现类
 * ConfirmCallback 交换机回调接口
 * ReturnsCallback 消息回退接口
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback,RabbitTemplate.ReturnsCallback {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * Callback为RabbitTemplate内部接口无法直接调用
	 * 当前类注入进RabbitTemplate
	 */
	@PostConstruct
	public void init(){
		rabbitTemplate.setConfirmCallback(this);
		rabbitTemplate.setReturnCallback(this);
	}

	/**
	 * 交换机确认回调
	 * @param correlationData 回调消息的ID及相关信息
	 * @param b 收到消息为true,失败为false
	 * @param s 失败原因,成功为null
	 */
	@Override
	public void confirm(CorrelationData correlationData, boolean b, String s) {
		// 不为空取出id
		String id = correlationData != null ? correlationData.getId() : "";
		// 是否成功
		if (b){
			log.info("交换机收到ID为:{}的消息",id);
		}else {
			log.info("交换机未收到ID为:{}的消息,原因为:{}",id,s);
		}
	}

	/**
	 * 当消息传递过程中不可达目的地时返回消费者
	 * @param message 消息
	 * @param replyCode 失败Code
	 * @param replyText 失败原因
	 * @param exchange 交换机
	 * @param routingKey key
	 */
	@Override
	public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
		log.error("消息:{},被交换机:{}退回,退回原因:{},路由Key:{}",new String(message.getBody()),exchange,replyText,routingKey);
	}

	@Override
	public void returnedMessage(ReturnedMessage returnedMessage) {
	}
}
