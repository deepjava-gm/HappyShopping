package com.changgou.order.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //添加积分任务交换机
    public static final String EX_BUYING_ADDPOINTUSER = "ex_buying_addpointuser";

    //添加积分消息队列
    public static final String CG_BUYING_ADDPOINT = "cg_buying_addpoint";

    //完成添加积分消息队列
    public static final String CG_BUYING_FINISHADDPOINT = "cg_buying_finishaddpoint";

    //添加积分路由key
    public static final String CG_BUYING_ADDPOINT_KEY = "addpoint";

    //完成添加积分路由key
    public static final String CG_BUYING_FINISHADDPOINT_KEY = "finishaddpoint";

    public static final String ORDER_PAY="order_pay";


    //    定义正常队列
    public static final String ORDER_QUEUE = "queue.ordercreate";

    //    定义死信队列
    public static final String ORDER_QUEUE_DLX = "queue.ordertimeout";

    //    定义死信交换机
    public static final String ORDER_EXCHANGE_DLX = "order_exchange_dlx";

    public static final String ORDER_TACK="order_tack";

    public static final String ORDER_PAY_NOTIFY="paynotify";


    //声明交换机
    @Bean(EX_BUYING_ADDPOINTUSER)
    public Exchange EX_BUYING_ADDPOINTUSER(){
        return ExchangeBuilder.directExchange(EX_BUYING_ADDPOINTUSER).durable(true).build();
    }

    //声明队列
    @Bean(CG_BUYING_ADDPOINT)
    public Queue CG_BUYING_ADDPOINT(){
        Queue queue = new Queue(CG_BUYING_ADDPOINT);
        return queue;
    }
    @Bean(CG_BUYING_FINISHADDPOINT)
    public Queue CG_BUYING_FINISHADDPOINT(){
        Queue queue = new Queue(CG_BUYING_FINISHADDPOINT);
        return queue;
    }

    //队列绑定交换机
    @Bean
    public Binding BINDING_CG_BUYING_ADDPOINT(@Qualifier(CG_BUYING_ADDPOINT) Queue queue,@Qualifier(EX_BUYING_ADDPOINTUSER)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CG_BUYING_ADDPOINT_KEY).noargs();
    }
    @Bean
    public Binding BINDING_CG_BUYING_FINISHADDPOINT(@Qualifier(CG_BUYING_FINISHADDPOINT) Queue queue,@Qualifier(EX_BUYING_ADDPOINTUSER)Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(CG_BUYING_FINISHADDPOINT_KEY).noargs();
    }

    @Bean
    public Queue queue(){
        return  new Queue(ORDER_PAY);
    }



    //    声明正常超时队列
    @Bean(ORDER_QUEUE)
    public Queue order1() {
        return QueueBuilder.durable(ORDER_QUEUE)
                .withArgument("x-dead-letter-exchange", ORDER_EXCHANGE_DLX) //死信交换机名称
                .withArgument("x-dead-letter-routing-key", "dlx.order.cancel") //死信队列的路由key
                .withArgument("x-message-ttl", 60000*10) // 设置队列的过期时间10分钟
                .build();
    }


    //    声明死信队列
    @Bean(ORDER_QUEUE_DLX)
    public Queue order2() {
        return QueueBuilder.durable(ORDER_QUEUE_DLX).build();
    }


    //  定义死信交换机
    @Bean(ORDER_EXCHANGE_DLX)
    public Exchange orderExchangeDLX() {
        return ExchangeBuilder.topicExchange(ORDER_EXCHANGE_DLX).durable(true).build();
    }


    //    绑定死信交换机和死信队列
    @Bean
    public Binding bindingExchange2(@Qualifier(ORDER_QUEUE_DLX) Queue queue,
                                    @Qualifier(ORDER_EXCHANGE_DLX) Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("dlx.order.#").noargs();
    }


    @Bean
    public Queue ORDER_TACK(){
        return  new Queue(ORDER_TACK);
    }

    //    支付成功 通知交换机
    @Bean
    public Exchange ORDER_PAY_NOTIFY(){
        return ExchangeBuilder.fanoutExchange(ORDER_PAY_NOTIFY).durable(true).build();
    }
}
