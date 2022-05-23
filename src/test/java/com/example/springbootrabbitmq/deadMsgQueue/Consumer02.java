package com.example.springbootrabbitmq.deadMsgQueue;

import com.example.springbootrabbitmq.utils.ConnectionFactoryUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 死信消费者
 */
public class Consumer02 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) {
        Channel channel = ConnectionFactoryUtil.getChannel();

        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println(new String(message.getBody(), StandardCharsets.UTF_8));
        };
        CancelCallback cancelCallback = (consumerTag) ->{
        };
        try {
            channel.basicConsume(DEAD_QUEUE,true,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
