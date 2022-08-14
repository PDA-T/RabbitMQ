package com.rabbitmq.springboot.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * TTL队列 配置文件
 */
@Configuration
public class TtlQueueConfig {
	// 交换机
	public static final String X_EXCHANGE = "X";
	// 死信交换机
	public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
	// 队列
	public static final String QUEUE_A = "QA";
	public static final String QUEUE_B = "QB";
	public static final String QUEUE_C = "QC";
	// 死信队列
	public static final String DEAD_LETTER_QUEUE = "QD";

	/**
	 * 声明交换机
	 * @return
	 */
	@Bean("xExchange")
	public DirectExchange xExchange(){
		return new DirectExchange(X_EXCHANGE);
	}

	/**
	 * 声明死信交换机
	 * @return
	 */
	@Bean("yExchange")
	public DirectExchange yExchange(){
		return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
	}

	/**
	 * 声明队列A
	 * TTL 10s
	 * @return
	 */
	@Bean("queueA")
	public Queue queueA(){
		Map<String,Object> arguments = new HashMap<String,Object>(3);
		// 设置死信交换机
		arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
		// 设置死信交换机Key
		arguments.put("x-dead-letter-routing-key","YD");
		// 设置TTL过期时间
		arguments.put("x-message-ttl",10000);
		return QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
	}

	/**
	 * 声明队列B
	 * TTL 40s
	 * @return
	 */
	@Bean("queueB")
	public Queue queueB(){
		Map<String,Object> arguments = new HashMap<String,Object>(3);
		// 设置死信交换机
		arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
		// 设置死信交换机Key
		arguments.put("x-dead-letter-routing-key","YD");
		// 设置TTL过期时间
		arguments.put("x-message-ttl",40000);
		return QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
	}

	/**
	 * 声明队列C
	 * @return
	 */
	@Bean("queueC")
	public Queue queueC(){
		Map<String,Object> arguments = new HashMap<String,Object>(3);
		// 设置死信交换机
		arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
		// 设置死信交换机Key
		arguments.put("x-dead-letter-routing-key","YD");
		return QueueBuilder.durable(QUEUE_C).withArguments(arguments).build();
	}

	/**
	 * 声明死信队列
	 * @return
	 */
	@Bean("queueD")
	public Queue queueD(){
		return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
	}

	/**
	 * 绑定关系
	 * @param queueA 队列A
	 * @param xExchange 交换机x
	 * @return
	 */
	@Bean
	public Binding queueABindingX(@Qualifier("queueA") Queue queueA,@Qualifier("xExchange") DirectExchange xExchange){
		return BindingBuilder.bind(queueA).to(xExchange).with("XA");
	}

	/**
	 * 绑定关系
	 * @param queueB 队列B
	 * @param xExchange 交换机x
	 * @return
	 */
	@Bean
	public Binding queueBBindingX(@Qualifier("queueB") Queue queueB,@Qualifier("xExchange") DirectExchange xExchange){
		return BindingBuilder.bind(queueB).to(xExchange).with("XB");
	}

	/**
	 * 绑定关系
	 * @param queueD 死信队列
	 * @param yExchange 死信交换机
	 * @return
	 */
	@Bean
	public Binding queueDBindingY(@Qualifier("queueD") Queue queueD,@Qualifier("yExchange") DirectExchange yExchange){
		return BindingBuilder.bind(queueD).to(yExchange).with("YD");
	}

	/**
	 * 绑定关系
	 * @param queueC 队列C
	 * @param xExchange 交换机
	 * @return
	 */
	@Bean
	public Binding queueCBindingX(@Qualifier("queueC") Queue queueC,@Qualifier("xExchange") DirectExchange xExchange){
		return BindingBuilder.bind(queueC).to(xExchange).with("XC");
	}
}
