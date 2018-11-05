package com.jichuangsi.school.courseservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.courseservice.model.AnswerForStudent;
import com.jichuangsi.school.courseservice.model.Course;
import com.jichuangsi.school.courseservice.model.Question;
import com.jichuangsi.school.courseservice.model.message.AnswerMessageModel;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;
import com.jichuangsi.school.courseservice.model.message.QuestionMessageModel;
import com.jichuangsi.school.courseservice.service.IMqService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class RabbitMqServiceImpl implements IMqService{

    @Resource
    private AmqpTemplate template;

    @Resource
    private RabbitAdmin rabbitAdmin;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RabbitMessagingTemplate rabbitMessagingTemplate;

    @Value("${com.jichuangsi.school.mq.courses}")
    private String coursesMq;

    @Value("${com.jichuangsi.school.mq.exchange}")
    private String exchange;

    @Value("${com.jichuangsi.school.mq.questions.pubilish}")
    private String questionsPubMq;

    @Value("${com.jichuangsi.school.mq.questions.terminate}")
    private String questionsTermMq;

    @Value("${com.jichuangsi.school.mq.answers}")
    private String answersMq;

    @Resource
    private ConnectionFactory connectionFactory;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
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

    @Override
    public void sendMsg4StartCourse(CourseMessageModel courseMsg){
        //rabbitMessagingTemplate.convertAndSend(exchange, coursesMq, JSONObject.parseObject(JSON.toJSONString(courseMsg), CourseMessageModel.class));
        logger.debug("Send " + coursesMq + " messgae:" + courseMsg.toString());
        rabbitMessagingTemplate.convertAndSend(exchange, coursesMq, JSON.toJSONString(courseMsg));
        logger.debug("Send " + coursesMq + "  messgae sucess");
    }

    @Override
    public void sendMsg4PublishQuestion(QuestionMessageModel questionMsg){
        logger.debug("Send " + questionsPubMq + " messgae:" + questionMsg.toString());
        rabbitMessagingTemplate.convertAndSend(exchange, questionsPubMq, JSON.toJSONString(questionMsg));
        logger.debug("Send " + questionsPubMq + "  messgae sucess");
    }

    @Override
    public void sendMsg4TermQuestion(QuestionMessageModel questionMsg) {
        logger.debug("Send " + questionsTermMq + " messgae:" + questionMsg.toString());
        rabbitMessagingTemplate.convertAndSend(exchange, questionsTermMq, JSON.toJSONString(questionMsg));
        logger.debug("Send " + questionsTermMq + "  messgae sucess");
    }

    @Override
    public void sendMsg4SubmitAnswer(AnswerMessageModel answerMsg){
        logger.debug("Send " + answersMq + " messgae:" + answerMsg.toString());
        rabbitMessagingTemplate.convertAndSend(exchange, answersMq, JSON.toJSONString(answerMsg));
        logger.debug("Send " + answersMq + "  messgae sucess");
    }

}


