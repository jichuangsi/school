/**
 * 
 */
package com.jichuangsi.school.statistics.mq.consumer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RabbitListener(queuesToDeclare = {
			@Queue(value = "${custom.mq.consumer.queue-name.courseStudentAdd}") })
	public void process(String jsonData) {
		AddToCourseModel addToCourseModel = JSONObject.parseObject(jsonData, AddToCourseModel.class);
		logger.debug("Receive courseStudentAdd messgae:" + jsonData);
		courseStatisticsService.addToCourse(addToCourseModel);
		logger.debug("courseStudentAdd messgae procss sucess.");
	}

}
