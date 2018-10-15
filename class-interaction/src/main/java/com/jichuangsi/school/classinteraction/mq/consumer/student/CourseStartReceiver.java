/**
 * 接收上课消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.student;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

	//queuesToDeclare会在队列不存在时自动创建；若使用queues，则会在队列不存在时报错（使用queues可以防止订阅的队列不存在，但需先启动生产者，调式部署时不便）
	@RabbitListener(queuesToDeclare = { @Queue(value = "${custom.mq.receiver.queue-name.course-start}") })
	public void process(String jsonData) {
		ClassInfoForStudent classInfoForStudent = JSONObject.parseObject(jsonData, ClassInfoForStudent.class);
		classInfoForStudent.setType(ClassInfoForStudent.TYPE_COURSE_START);
		sendToStudentService.sendClassInfo(classInfoForStudent);
	}
}
