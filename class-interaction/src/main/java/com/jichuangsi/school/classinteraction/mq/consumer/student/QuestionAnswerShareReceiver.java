package com.jichuangsi.school.classinteraction.mq.consumer.student;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.mq.consumer.AbstractReceiver;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionAnswerShare;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;


@Component
public class QuestionAnswerShareReceiver extends AbstractReceiver{
	
	@Resource
	private ISendToStudentService sendToStudentService;
	
	@Override
	public void process(String jsonData) {
		QuestionAnswerShare info = JSONObject.parseObject(jsonData, QuestionAnswerShare.class);
		sendToStudentService.sendQuestionAnswerShareInfo(info);
	}
	
	@Override
	@RabbitListener(queuesToDeclare = {
			@Queue(value = "${custom.mq.consumer.queue-name.answer-share}") })
	public void processWithLog(String jsonData) {
		super.processWithLog(jsonData);
	}

}
