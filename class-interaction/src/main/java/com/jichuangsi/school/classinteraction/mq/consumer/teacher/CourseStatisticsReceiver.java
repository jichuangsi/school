/**
 * 接收课堂统计信息变更消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.teacher;

import javax.annotation.Resource;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.websocket.model.CourseStatistics;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Component
public class CourseStatisticsReceiver {
	@Resource
	private ISendToStudentService sendToStudentService;
	
	@Value("${custom.mq.receiver.queue-name.course-statistics}")
	private String queueName;

	@RabbitListener(queues = "${custom.mq.receiver.queue-name.course-statistics}")
	public void process(String jsonData) {
		CourseStatistics info = JSONObject.parseObject(jsonData, CourseStatistics.class);
		sendToStudentService.sendCourseStatisticsInfo(info);
	}

	@Bean
	public Queue queue() {
		return new Queue(queueName);
	}
}
