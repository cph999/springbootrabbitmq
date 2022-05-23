package com.example.springbootrabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

public class ConnectionFactoryUtil {
    @SneakyThrows
    public static Channel getChannel(){
        //创建连接工厂
        ConnectionFactory connectionFactory = new ConnectionFactory();
        //rabbitmq连接信息
        connectionFactory.setHost("121.196.223.94");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        Connection connection = null;
        Channel channel = null;
        connection = connectionFactory.newConnection();
        channel = connection.createChannel();
        return  channel;
    }
}
