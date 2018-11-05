/**
 * 接收学生作答消息
 */
package com.jichuangsi.school.statistics.mq.consumer;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.model.StudentAnswerModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;

/**
 * @author huangjiajun
 *
 */
@Component
public class StudentAnswerReceiver {
	@Resource
	private ICourseStatisticsService courseStatisticsService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@RabbitListener(queuesToDeclare = {
			@Queue(value = "${custom.mq.consumer.queue-name.answerQuestion}", autoDelete = "true") })
	public void process(String jsonData) {
		StudentAnswerModel model = JSONObject.parseObject(jsonData, StudentAnswerModel.class);
		logger.debug("Receive answerQuestion messgae:" + jsonData);
		courseStatisticsService.saveStudentAnswer(model);
		logger.debug("answerQuestion messgae procss sucess.");
	}
}
