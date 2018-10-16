/**
 * 接收学生作答消息
 */
package com.jichuangsi.school.statistics.mq.consumer;

import javax.annotation.Resource;

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
	
	@RabbitListener(queuesToDeclare = { @Queue(value = "${custom.mq.consumer.queue-name.answerQuestion}") })
	public void process(String jsonData) {
		StudentAnswerModel model = JSONObject.parseObject(jsonData, StudentAnswerModel.class);
		courseStatisticsService.saveStudentAnswer(model);
	}
}
