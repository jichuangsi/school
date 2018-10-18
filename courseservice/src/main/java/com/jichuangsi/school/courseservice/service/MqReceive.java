package com.jichuangsi.school.courseservice.service;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class MqReceive {
    //@RabbitListener(queues="${com.jichuangsi.school.mq.courses}")    //监听器监听指定的Queue
    public void processC(JSONObject str) {
        System.out.println("Receive:"+str.toJSONString());
    }
    //@RabbitListener(queues="${com.jichuangsi.school.mq.questions}")    //监听器监听指定的Queue
    public void processQ(JSONObject str) {
        System.out.println("Receive:"+str.toJSONString());
    }
}
