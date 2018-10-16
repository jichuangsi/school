/**
 * 接收发布题目消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.student;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionForPublish;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Component
public class QusetionPublishReceiver {
	@Resource
	private ISendToStudentService sendToStudentService;

	@RabbitListener(queuesToDeclare = { @Queue(value = "${custom.mq.consumer.queue-name.question-pubilish}") })
	public void process(String jsonData) {
		QuestionForPublish questionForPublish = JSONObject.parseObject(jsonData, QuestionForPublish.class);
		sendToStudentService.sendPubQuestionInfo(questionForPublish);
	}
}
