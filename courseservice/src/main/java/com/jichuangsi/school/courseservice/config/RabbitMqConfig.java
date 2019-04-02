package com.jichuangsi.school.courseservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;

@Configuration
public class RabbitMqConfig {

    @Value("${com.jichuangsi.school.mq.courses}")
    private String coursesMq;

    @Value("${com.jichuangsi.school.mq.questions.pubilish}")
    private String questionsPubMq;

    @Value("${com.jichuangsi.school.mq.questions.terminate}")
    private String questionsTermMq;

    @Value("${com.jichuangsi.school.mq.answers.statistics}")
    private String answersStatisticsMq;

    @Value("${com.jichuangsi.school.mq.answers.share}")
    private String answersShareMq;

    @Value("${com.jichuangsi.school.mq.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.username}")
    private String username;

    @Value("${spring.rabbitmq.password}")
    private String password;

    @Value("${spring.rabbitmq.port}")
    private String port;

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${com.jichuangsi.school.mq.publish}")
    private String publishFile;

    @Bean(name="courses")
    public Queue coursesMessage(RabbitAdmin rabbitAdmin) {
        /*Map<String, Object> argss = new HashMap<String, Object>();
        argss.put("x-message-ttl",5000l);*/
        Queue courses = new Queue(coursesMq,true, false, false);
        rabbitAdmin.declareQueue(courses);
        return courses;
    }

    @Bean(name="publishFile")
    public Queue publishFileMessage(RabbitAdmin rabbitAdmin) {
        Queue publish = new Queue(publishFile,true, false, false);
        rabbitAdmin.declareQueue(publish);
        return publish;
    }

    @Bean(name="questionsPub")
    public Queue questionsPubMessage(RabbitAdmin rabbitAdmin) {
        Queue questions = new Queue(questionsPubMq,true, false, false);
        rabbitAdmin.declareQueue(questions);
        return questions;
    }

    @Bean(name="questionsTerm")
    public Queue questionsTermMessage(RabbitAdmin rabbitAdmin) {
        Queue questions = new Queue(questionsTermMq,true, false, false);
        rabbitAdmin.declareQueue(questions);
        return questions;
    }

    @Bean(name="answersStatistics")
    public Queue answersStatisticsMessage(RabbitAdmin rabbitAdmin) {
        Queue answers = new Queue(answersStatisticsMq,true, false, false);
        rabbitAdmin.declareQueue(answers);
        return answers;
    }

    @Bean(name="answersShare")
    public Queue answersShareMessage(RabbitAdmin rabbitAdmin) {
        Queue answers = new Queue(answersShareMq,true, false, false);
        rabbitAdmin.declareQueue(answers);
        return answers;
    }

    @Bean
    public TopicExchange exchange(RabbitAdmin rabbitAdmin) {
        TopicExchange topicExchange = new TopicExchange(exchange);
        rabbitAdmin.declareExchange(topicExchange);
        return topicExchange;
    }

    @Bean
    public Binding courseMqBinding(RabbitAdmin rabbitAdmin) {
        return BindingBuilder.bind(coursesMessage(rabbitAdmin)).to(exchange(rabbitAdmin)).with(coursesMq);
    }

    @Bean
    public Binding questionsPubMqBinding(RabbitAdmin rabbitAdmin) {
        return BindingBuilder.bind(questionsPubMessage(rabbitAdmin)).to(exchange(rabbitAdmin)).with(questionsPubMq);
    }

    @Bean
    public Binding questionsTermMqBinding(RabbitAdmin rabbitAdmin) {
        return BindingBuilder.bind(questionsTermMessage(rabbitAdmin)).to(exchange(rabbitAdmin)).with(questionsTermMq);
    }

    @Bean
    public Binding answersStatisticsMqBinding(RabbitAdmin rabbitAdmin) {
        return BindingBuilder.bind(answersStatisticsMessage(rabbitAdmin)).to(exchange(rabbitAdmin)).with(answersStatisticsMq);
    }

    @Bean
    public Binding answersShareMqBinding(RabbitAdmin rabbitAdmin) {
        return BindingBuilder.bind(answersShareMessage(rabbitAdmin)).to(exchange(rabbitAdmin)).with(answersShareMq);
    }

    /*@Bean
    public ConnectionFactory connectionFactory(){
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost("/");
        connectionFactory.setHost(host);

        return connectionFactory;
    }*/

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitMessagingTemplate rabbitMessagingTemplate(RabbitTemplate rabbitTemplate) {
        RabbitMessagingTemplate rabbitMessagingTemplate = new RabbitMessagingTemplate();
        rabbitMessagingTemplate.setMessageConverter(jackson2Converter());
        rabbitMessagingTemplate.setRabbitTemplate(rabbitTemplate);
        return rabbitMessagingTemplate;
    }

    @Bean
    public MappingJackson2MessageConverter jackson2Converter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        return converter;
    }
}
