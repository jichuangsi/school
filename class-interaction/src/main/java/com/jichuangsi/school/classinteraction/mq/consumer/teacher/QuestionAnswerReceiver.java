/**
 * 
 */
package com.jichuangsi.school.classinteraction.mq.consumer.teacher;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.model.StudentAnswerModel;
import com.jichuangsi.school.classinteraction.mq.consumer.AbstractReceiver;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToTeacherService;

/**
 * @author huangjiajun
 *
 */
@Component
public class QuestionAnswerReceiver extends AbstractReceiver {
	@Resource
	private ISendToTeacherService sendToTeacherService;

	@Override
	public void process(String jsonData) {
		StudentAnswerModel info = JSONObject.parseObject(jsonData, StudentAnswerModel.class);
		sendToTeacherService.sendQuestionAnswerInfo(info);
	}
	
	@Override
	@RabbitListener(queuesToDeclare = { @Queue(value = "${custom.mq.consumer.queue-name.question-answer}") })
	public void processWithLog(String jsonData) {
		super.processWithLog(jsonData);
	}
}
