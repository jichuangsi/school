/**
 * 接收上课消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.student;

import javax.annotation.Resource;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.websocket.model.ClassInfoForStudent;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Component
public class CourseStartReceiver {

	@Resource
	private ISendToStudentService sendToStudentService;
	
	@Value("${custom.mq.queue-name.course-start}")
	private String queueName;

	@RabbitListener(queues = "${custom.mq.queue-name.course-start}")
	public void process(String jsonData) {
		ClassInfoForStudent classInfoForStudent = JSONObject.parseObject(jsonData, ClassInfoForStudent.class);
		classInfoForStudent.setType(ClassInfoForStudent.TYPE_COURSE_START);
		sendToStudentService.sendClassInfo(classInfoForStudent);
	}

	@Bean
	public Queue queue() {
		return new Queue(queueName);
	}
}
