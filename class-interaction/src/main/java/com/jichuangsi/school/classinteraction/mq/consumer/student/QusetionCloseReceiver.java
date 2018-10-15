/**
 * 接收终止作答消息
 */
package com.jichuangsi.school.classinteraction.mq.consumer.student;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionClose;
import com.jichuangsi.school.classinteraction.websocket.service.ISendToStudentService;

/**
 * @author huangjiajun
 *
 */
@Component
public class QusetionCloseReceiver {
	@Resource
	private ISendToStudentService sendToStudentService;

	@RabbitListener(queuesToDeclare = { @Queue(value = "${custom.mq.receiver.queue-name.question-termin}") })
	public void process(String jsonData) {
		QuestionClose questionClose = JSONObject.parseObject(jsonData, QuestionClose.class);
		sendToStudentService.sendQuestionCloseInfo(questionClose);
	}
}
