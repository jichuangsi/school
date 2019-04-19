/**
 * 接收学生作答消息
 */
package com.jichuangsi.school.statistics.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.model.StudentAnswerModel;
import com.jichuangsi.school.statistics.model.result.performance.StudentCoursePerformanceMessageModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;
import com.jichuangsi.school.statistics.service.IStudentStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author huangjiajun
 *
 */
@Component
public class StudentPerformanceReceiver {
	@Resource
	private IStudentStatisticsService studentStatisticsService;
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@RabbitListener(queuesToDeclare = {
			@Queue(value = "${custom.mq.consumer.queue-name.performance}") })
	public void process(String jsonData) {
		logger.debug("Receive answerQuestion messgae:" + jsonData);
		if(JSONObject.parseObject(jsonData).getString("msgType").equalsIgnoreCase("COURSE")){
			StudentCoursePerformanceMessageModel model = JSONObject.parseObject(jsonData, StudentCoursePerformanceMessageModel.class);
			studentStatisticsService.saveStudentCoursePerformance(model);
		}
		//courseStatisticsService.saveStudentAnswer(model);
		logger.debug("answerQuestion messgae procss sucess.");
	}
}
