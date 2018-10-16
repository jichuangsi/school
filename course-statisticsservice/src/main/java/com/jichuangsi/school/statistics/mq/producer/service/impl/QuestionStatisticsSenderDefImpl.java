/**
 * 
 */
package com.jichuangsi.school.statistics.mq.producer.service.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.model.QuestionStatisticsInfo;
import com.jichuangsi.school.statistics.mq.producer.service.IQuestionStatisticsSender;

/**
 * @author huangjiajun
 *
 */
@Service
public class QuestionStatisticsSenderDefImpl implements IQuestionStatisticsSender{
	@Resource
	private AmqpTemplate rabbitTemplate;

	@Value("${custom.mq.producer.queue-name.questionStatistics}")
	private String questionStatistics;

	@Override
	public void send(QuestionStatisticsInfo questionStatisticsInfo) {
		rabbitTemplate.convertAndSend(questionStatistics, JSONObject.toJSONString(questionStatisticsInfo));
	}
}
