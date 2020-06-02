package com.itheima.canal.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.listener.QueuesNotAvailableException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {


    //定义队列名称
    public static final String AD_UPDATE_QUEUE="ad_update_queue";

    //声明队列
    @Bean
    public Queue queue(){
        return new Queue(AD_UPDATE_QUEUE);
    }




}