/**
 * 
 */
package com.jichuangsi.school.statistics.mq.consumer;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.model.AddToCourseModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;

/**
 * @author huangjiajun
 *
 */
@Component
public class StudentAddToCourseReceiver {

	@Resource
	private ICourseStatisticsService courseStatisticsService;

	@RabbitListener(queuesToDeclare = {
			@Queue(value = "${custom.mq.consumer.queue-name.courseStudentAdd}", autoDelete = "true") })
	public void process(String jsonData) {
		AddToCourseModel addToCourseModel = JSONObject.parseObject(jsonData, AddToCourseModel.class);
		courseStatisticsService.addToCourse(addToCourseModel);
	}

}
