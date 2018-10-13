package com.jichuangsi.school.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.Course;
import com.jichuangsi.school.courseservice.model.Question;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;
import com.jichuangsi.school.courseservice.service.IMqService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqServiceImpl implements IMqService{

    @Autowired
    private AmqpTemplate template;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Value("${com.jichuangsi.school.mq.courses}")
    private String coursesMq;

    @Value("${com.jichuangsi.school.mq.exchange}")
    private String exchange;

    @Autowired
    private ConnectionFactory connectionFactory;

    public void send(Course course) {
        /*Map<String, Object> argss = new HashMap<String, Object>();
        argss.put("x-message-ttl",5000l);
        Queue queue = new Queue("queue2",true, false, false, argss);
        String tst = rabbitAdmin.declareQueue(queue);
        TopicExchange topicExchange = new TopicExchange("exchange");
        rabbitAdmin.declareExchange(topicExchange);
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with("queue2");
        rabbitAdmin.declareBinding(binding);
        rabbitMessagingTemplate.convertAndSend("exchange", "queue2", JSONObject.parseObject(JSON.toJSONString(course), Course.class));*/
        //rabbitTemplate.setQueue("queue");
        //rabbitTemplate.convertAndSend("hello,rabbit~");
        //template.convertAndSend(coursesMq,"hello,rabbit~");

        rabbitMessagingTemplate.convertAndSend(exchange, coursesMq, JSONObject.parseObject(JSON.toJSONString(course), Course.class));
    }

    public void sendMsg4StartCourse(CourseMessageModel courseMsg){
        rabbitMessagingTemplate.convertAndSend(exchange, coursesMq, JSONObject.parseObject(JSON.toJSONString(courseMsg), CourseMessageModel.class));
    }

    public void sendMsg4PublishQuestion(String courseId, String questionId){

    }

    public void sendMsg4SubmitAnswer(String courseId, String questionId, AnswerForStudent answer){

    }

}


