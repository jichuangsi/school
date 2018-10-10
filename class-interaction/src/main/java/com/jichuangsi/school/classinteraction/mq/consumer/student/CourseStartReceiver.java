/**
 * 接收上课消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.student;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author huangjiajun
 *
 */
@Component
@RabbitListener(queues = "course.start.classinteraction")
public class CourseStartReceiver {

	
    @RabbitHandler
    public void process(String hello) {
        //
    }
}
