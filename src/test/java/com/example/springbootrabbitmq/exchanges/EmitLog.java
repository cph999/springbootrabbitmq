package com.example.springbootrabbitmq.exchanges;

import com.example.springbootrabbitmq.utils.ConnectionFactoryUtil;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class EmitLog {
    public static final String EXCHANGE_NAME = "logs";

    public static void main(String[] args) {
        Channel channel = ConnectionFactoryUtil.getChannel();
        try {
            channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNext()){
                String s = scanner.next();
                channel.basicPublish(EXCHANGE_NAME,"acking",null,s.getBytes(StandardCharsets.UTF_8));
                System.out.println("生产者发出消息：" + s);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
