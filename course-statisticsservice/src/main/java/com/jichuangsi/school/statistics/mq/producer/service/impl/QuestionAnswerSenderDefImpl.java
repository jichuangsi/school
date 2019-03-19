/**
 * 
 */
package com.jichuangsi.school.statistics.mq.producer.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.model.StudentAnswerModel;
import com.jichuangsi.school.statistics.mq.producer.service.IQuestionAnswerSender;

/**
 * @author huangjiajun
 *
 */
@Service
public class QuestionAnswerSenderDefImpl implements IQuestionAnswerSender {
	
	@Resource
	private AmqpTemplate rabbitTemplate;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${custom.mq.producer.queue-name.questionAnswer}")
	private String questionAnswerMsgName;

	@Override
	public void send(StudentAnswerModel studentAnswerModel) {
		String msg = JSONObject.toJSONString(studentAnswerModel);
		logger.debug("Send " + questionAnswerMsgName + " messgae:" + msg);
		rabbitTemplate.convertAndSend(questionAnswerMsgName, msg);
		logger.debug("Send " + questionAnswerMsgName + " messgae sucess");

	}

}
