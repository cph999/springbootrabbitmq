package com.example.springbootrabbitmq.deadMsgQueue;

import com.example.springbootrabbitmq.utils.ConnectionFactoryUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.SneakyThrows;

/**
 * 死信生产者代码
 */
public class Producer {


    //普通交换机名称
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    @SneakyThrows
    public static void main(String[] args) {
        Channel channel = ConnectionFactoryUtil.getChannel();
        //设置ttl 10s
        AMQP.BasicProperties properties = new AMQP.BasicProperties()
                .builder().expiration("10000").build();
        for(int i = 0; i < 11; i++){
            String msg = "info" + i;
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,msg.getBytes());
        }
    }
}
