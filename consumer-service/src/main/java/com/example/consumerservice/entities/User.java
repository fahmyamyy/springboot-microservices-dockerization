//package com.example.consumerservice.entities;
//
//import com.example.consumerservice.configs.MessagingConfig;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class User {
//
//    @RabbitListener(queues = MessagingConfig.QUEUE)
//    public void consumeMessageFromQueue(OrderStatus orderStatus) {
//        System.out.println("Message recieved from queue : " + orderStatus);
//    }
//}