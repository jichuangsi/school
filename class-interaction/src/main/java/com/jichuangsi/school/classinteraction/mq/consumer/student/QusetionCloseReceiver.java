/**
 * 接收终止作答消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.student;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.mq.consumer.AbstractReceiver;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionClose;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Component
public class QusetionCloseReceiver extends AbstractReceiver {
	@Resource
	private ISendToStudentService sendToStudentService;

	@Override
	public void process(String jsonData) {
		QuestionClose questionClose = JSONObject.parseObject(jsonData, QuestionClose.class);
		sendToStudentService.sendQuestionCloseInfo(questionClose);
	}

	@Override
	// queuesToDeclare会在队列不存在时自动创建；若使用queues，则会在队列不存在时报错（使用queues可以防止订阅的队列不存在，但需先启动生产者，调式部署时不便）
	@RabbitListener(queuesToDeclare = {
			@Queue(value = "${custom.mq.consumer.queue-name.question-termin}", autoDelete = "true") })
	public void processWithLog(String jsonData) {
		super.processWithLog(jsonData);
	}
}
