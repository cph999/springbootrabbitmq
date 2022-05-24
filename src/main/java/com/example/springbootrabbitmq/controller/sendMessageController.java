package com.example.springbootrabbitmq.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * 发送延迟消息
 */
@Controller
@Slf4j
public class sendMessageController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RequestMapping("/ttl/sendMsg/{message}")
    public void sendMessage(@PathVariable String  message){
        log.info("当前时间：{}，发送一条消息给两个队列:{}",new Date().toString(),message);
        rabbitTemplate.convertAndSend("X","XA","消息来自ttl 1s的队列"+message);
        rabbitTemplate.convertAndSend("X","XB","消息来自ttl 2s的队列"+message);
        log.info("发送完毕");
    }
}
