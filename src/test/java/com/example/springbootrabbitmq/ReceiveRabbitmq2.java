package com.example.springbootrabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveRabbitmq2 {
    public static void main(String[] args) {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //rabbitmq连接信息
        connectionFactory.setHost("121.196.223.94");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");


        try{
            final Connection connection = connectionFactory.newConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare("myQueue", true, false, false, null);
            /**
             * 接收消息
             */

            //接受成功调用的回调函数
            DeliverCallback deliverCallback = (consumerTag, message) -> {
                System.out.println(new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
            };
            //接受失败调用的回调函数
            CancelCallback cancelCallback = (consumeTag) -> {
                System.out.println("消息被中断" + consumeTag);
                /**
                 * 手动应答
                 */
                channel.basicCancel(consumeTag);
            };

            channel.basicConsume("myQueue",false,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
}
