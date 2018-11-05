/**
 * 接收课堂统计信息变更消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.teacher;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.mq.consumer.AbstractReceiver;
import com.jichuangsi.school.classinteraction.websocket.model.CourseStatistics;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToTeacherService;

/**
 * @author huangjiajun
 *
 */
@Component
public class CourseStatisticsReceiver extends AbstractReceiver{
	@Resource
	private ISendToTeacherService sendToTeacherService;

	@Override
	public void process(String jsonData) {
		CourseStatistics info = JSONObject.parseObject(jsonData, CourseStatistics.class);
		sendToTeacherService.sendCourseStatisticsInfo(info);
	}
	
	@Override
	@RabbitListener(queuesToDeclare = { @Queue(value = "${custom.mq.consumer.queue-name.course-statistics}", autoDelete = "true") })
	public void processWithLog(String jsonData) {
		super.processWithLog(jsonData);
	}

}
