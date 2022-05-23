package com.example.springbootrabbitmq.exchanges;

import com.example.springbootrabbitmq.utils.ConnectionFactoryUtil;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveLog1 {

    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) {
        Channel channel = ConnectionFactoryUtil.getChannel();
        //声明一个交换机
        try {
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            //声明一个临时队列(队列名称随机，当消费者断开连接时，队列自动删除)
            String queueName = channel.queueDeclare().getQueue();
            //绑定交换机与队列
            channel.queueBind(queueName,EXCHANGE_NAME,"");
            //接收消息回调接口
            DeliverCallback deliverCallback = (consumerTag, message) ->{
                System.out.println("消费者1接收消息:" + new String(message.getBody(), StandardCharsets.UTF_8));
            };

            CancelCallback cancelCallback = (consumerTag) -> {
                System.out.println(new String("拒绝消息：" + consumerTag));
            };
            //消费者取消消息回调接口
            channel.basicConsume(queueName,true,deliverCallback,cancelCallback);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
