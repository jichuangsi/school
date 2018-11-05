/**
 * 接收上课消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.student;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.mq.consumer.AbstractReceiver;
import com.jichuangsi.school.classinteraction.websocket.model.ClassInfoForStudent;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Component
public class CourseStartReceiver extends AbstractReceiver {

	@Resource
	private ISendToStudentService sendToStudentService;

	@Override
	public void process(String jsonData) {
		ClassInfoForStudent classInfoForStudent = JSONObject.parseObject(jsonData, ClassInfoForStudent.class);
		classInfoForStudent.setType(ClassInfoForStudent.TYPE_COURSE_START);
		sendToStudentService.sendClassInfo(classInfoForStudent);
	}

	@Override
	@RabbitListener(queuesToDeclare = {
			@Queue(value = "${custom.mq.consumer.queue-name.course-start}", autoDelete = "true") })
	public void processWithLog(String jsonData) {
		super.processWithLog(jsonData);
	}
}
