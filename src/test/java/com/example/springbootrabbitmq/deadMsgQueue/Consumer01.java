package com.example.springbootrabbitmq.deadMsgQueue;

import com.example.springbootrabbitmq.utils.ConnectionFactoryUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * 消息进入死信队列的原因：
 * 1. 消息被拒绝
 * 2. 消息TTL过期
 * 3. 队列达到最大长度
 */
public class Consumer01 {

    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    //死信交换机
    public static final String DEAD_EXCHANGE = "dead_exchange";
    //普通队列名称
    public static final String NORMAL_QUEUE = "normal_queue";
    //死信队列名称
    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) {
        Channel channel = ConnectionFactoryUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag,  message) ->{
            String s = new String(message.getBody(), StandardCharsets.UTF_8);
            System.out.println(s);
            if("info5".equals(s)){
                System.out.println(s+"被拒绝");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);
            }else{
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            }
        };
        CancelCallback cancelCallback = (consumerTag) ->{
        };
        try {
            //声明交换机
            channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
            channel.exchangeDeclare(DEAD_EXCHANGE, BuiltinExchangeType.DIRECT);

            //声明普通队列
            HashMap<String, Object> arguments = new HashMap<>();
            arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
            arguments.put("x-dead-letter-routing-key","lisi");
            arguments.put("x-message-ttl",1000*10);
            channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
            //声明死信队列
            channel.queueDeclare(DEAD_QUEUE,false,false,false,null);

            //绑定普通交换机与队列
            channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");
            //绑定死信交换机与队列
            channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
            System.out.println("Consumer1接收的消息是：.....");
            channel.basicConsume(NORMAL_QUEUE,false,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
