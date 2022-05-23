package com.example.springbootrabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import lombok.SneakyThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

class Producer {

    @SneakyThrows
    public static void main(String[] args)  {
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //rabbitmq连接信息
        connectionFactory.setHost("121.196.223.94");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = null;
        Channel channel = null;

        try {
            connection = connectionFactory.newConnection(); //获取链接
            channel = connection.createChannel();
            /**
             * 参数1：队列名称
             * 参数2：是否持久化队列
             * 参数3：是否排外，如果排外则这个队列只允许一个消费者链接
             * 参数4：是否自动删除，如果为true，表示当前队列中没有消息且没有消费者链接则自动删除该队列
             * 参数5：定义队列的一些属性（ttl）
             */
            channel.queueDeclare("myQueue", true, false, false, null);
            String message = "崇鹏豪666";
            /**
             * 发送消息到mq
             * 参数1： 交换机名称 ”“表示不适用交换机
             * 参数2： 队列名称或者routingkey，
             * 参数3： 消息属性信息,可以设置消息持久化
             * 参数4： 消息内容
             */
            channel.basicPublish("", "myQueue", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes(StandardCharsets.UTF_8));
            System.out.println("消息发送成功");
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TimeoutException e) {
            throw new RuntimeException(e);
        }finally {
            channel.close();
            connection.close();
        }
    }
}
