package com.example.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * @author Chong
 * @Description: Ttl队列
 */
@Configuration
public class TtlQueueConfig {
    //普通交换机名称
    public static final String X_EXCHANGE = "X";
    //死信交换机名称
    public static final String Y_DEAD_LETTER_EXCHANGE = "Y";
    //普通队列名称
    public static final String QUEUE_A = "QA";
    public static final String QUEUE_B = "QB";
    //死信队列名称
    public static final String DEAD_LETTER_QUEUE_D = "QD";

    @Bean("xExchange")
    public DirectExchange xExchange(){
        return new DirectExchange(X_EXCHANGE);
    }
    @Bean("yExchange")
    public DirectExchange yExchange(){
        return new DirectExchange(Y_DEAD_LETTER_EXCHANGE);
    }
    //声明队列
    @Bean("queueA")
    public Queue queueA(){
        HashMap<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",1000*10);
        return  QueueBuilder.durable(QUEUE_A).withArguments(arguments).build();
    }
    @Bean("queueB")
    public Queue queueB(){
        HashMap<String, Object> arguments = new HashMap<>(3);
        arguments.put("x-dead-letter-exchange",Y_DEAD_LETTER_EXCHANGE);
        arguments.put("x-dead-letter-routing-key","YD");
        arguments.put("x-message-ttl",4000*10);
        return  QueueBuilder.durable(QUEUE_B).withArguments(arguments).build();
    }

    //死信
    @Bean("queueD")
    public Queue queueD(){
        return  QueueBuilder.durable(DEAD_LETTER_QUEUE_D).build();
    }
    @Bean
    public Binding queueABindingX(@Qualifier("queueA") Queue queue,
                                  @Qualifier("xExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("XA");
    }

    @Bean
    public Binding queueBBindingX(@Qualifier("queueB") Queue queue,
                                  @Qualifier("xExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("XB");
    }
    @Bean
    public Binding queueABindingD(@Qualifier("queueD") Queue queue,
                                  @Qualifier("yExchange") DirectExchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with("YD");
    }
}
