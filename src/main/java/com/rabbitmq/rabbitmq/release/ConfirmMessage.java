package com.rabbitmq.rabbitmq.release;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.rabbitmq.rabbitmq.utils.RabbitMqUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 发布确认模式
 * 单个
 * 批量
 * 异步
 */
public class ConfirmMessage {

	// 批量发消息的个数
	public static final int MESSAGE_COUNT = 1000;

	public static void main(String[] args) throws Exception {
		// 单个确认
		ConfirmMessage.publishMassageIndividually();// 耗时:16319ms
		// 批量确认
		ConfirmMessage.publishMassageBatch();// 耗时:231ms
		// 异步确认
		ConfirmMessage.publishMassageAsync();// 耗时:46ms
	}

	/**
	 * 单个确认
	 */
	public static void publishMassageIndividually() throws Exception{
		Channel channel = RabbitMqUtils.getChannel();
		// 队列名称
		String queueName = UUID.randomUUID().toString();
		// 信道持久化
		channel.queueDeclare(queueName,true,false,false,null);
		// 开启发布确认
		channel.confirmSelect();
		// 开始时间
		Long begin = System.currentTimeMillis();
		// 循环1000
		for (int i = 0;i<MESSAGE_COUNT;i++){
			// 消息
			String message = i + "";
			// 发送消息
			channel.basicPublish("",queueName,null,message.getBytes());
			// 发布确认
			boolean flag = channel.waitForConfirms();
			if (flag){
				System.out.println("消息发送成功");
			}
		}
		// 结束时间
		Long end = System.currentTimeMillis();
		System.out.println("发布" + MESSAGE_COUNT + "个单独确认消息,耗时:" + (end - begin) + "ms");
	}

	/**
	 * 批量发布确认
	 */
	public static void publishMassageBatch() throws Exception{
		Channel channel = RabbitMqUtils.getChannel();
		// 队列名称
		String queueName = UUID.randomUUID().toString();
		// 信道持久化
		channel.queueDeclare(queueName,true,false,false,null);
		// 开启发布确认
		channel.confirmSelect();
		// 开始时间
		Long begin = System.currentTimeMillis();
		// 批量确认消息大小
		int batchSize = 100;
		// 循环1000
		for (int i = 0;i<MESSAGE_COUNT;i++){
			// 消息
			String message = i + "";
			// 发送消息
			channel.basicPublish("",queueName,null,message.getBytes());
			// 达到100条消息确认
			if (i % batchSize == 0){
				// 发布确认
				boolean flag = channel.waitForConfirms();
				if (flag){
					System.out.println("消息发送成功");
				}
			}
		}
		// 结束时间
		Long end = System.currentTimeMillis();
		System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时:" + (end - begin) + "ms");
	}

	/**
	 * 异步确认
	 */
	public static void publishMassageAsync() throws Exception{
		Channel channel = RabbitMqUtils.getChannel();
		// 队列名称
		String queueName = UUID.randomUUID().toString();
		// 信道持久化
		channel.queueDeclare(queueName,true,false,false,null);
		// 开启发布确认
		channel.confirmSelect();
		/*
		 * 线程安全有序的一个哈希表,适用于高并发
		 * 将序号与消息关联
		 * 批量删除消息
		 * 支持高并发
		 */
		ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<Long,String>();
		/*
		 * 消息成功监听
		 * deliveryTag 消息的标记
		 * multiple 是否为批量确认
		 */
		ConfirmCallback ackCallback = (deliveryTag,multiple) -> {
			// 是否为批量消息
			if (multiple){
				// 获取消息
				ConcurrentNavigableMap<Long, String> confirmed = outstandingConfirms.headMap(deliveryTag);
				// 清理消息
				confirmed.clear();
			}else {
				// 删除消息
				outstandingConfirms.remove(deliveryTag);
			}
			System.out.println("确认的消息:" + deliveryTag);
		};
		/*
		 * 消息失败监听
		 * deliveryTag 消息的标记
		 * multiple 是否为批量确认
		 */
		ConfirmCallback nackCallback = (deliveryTag,multiple) -> {
			// 获取消息
			String message = outstandingConfirms.get(deliveryTag);
			System.out.println("未确认的消息:" + message);
		};
		// 消息监听器
		channel.addConfirmListener(ackCallback,nackCallback);
		// 开始时间
		Long begin = System.currentTimeMillis();
		// 循环1000
		for (int i = 0;i<MESSAGE_COUNT;i++){
			// 消息
			String message = i + "";
			// 发送消息
			channel.basicPublish("",queueName,null,message.getBytes());
			// 记录消息,序号和消息
			outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
		}
		// 结束时间
		Long end = System.currentTimeMillis();
		System.out.println("发布" + MESSAGE_COUNT + "个异步确认消息,耗时:" + (end - begin) + "ms");
	}
}
