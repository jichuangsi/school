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
import com.jichuangsi.school.statistics.model.QuestionStatisticsInfoModel;
import com.jichuangsi.school.statistics.mq.producer.service.IQuestionStatisticsSender;

/**
 * @author huangjiajun
 *
 */
@Service
public class QuestionStatisticsSenderDefImpl implements IQuestionStatisticsSender {
	@Resource
	private AmqpTemplate rabbitTemplate;

	@Value("${custom.mq.producer.queue-name.questionStatistics}")
	private String questionStatistics;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void send(QuestionStatisticsInfoModel questionStatisticsInfo) {
		String msg = JSONObject.toJSONString(questionStatisticsInfo);
		logger.debug("Send " + questionStatistics + " messgae:" + msg);
		rabbitTemplate.convertAndSend(questionStatistics, msg);
		logger.debug("Send " + questionStatistics + " messgae sucess");
	}
}
