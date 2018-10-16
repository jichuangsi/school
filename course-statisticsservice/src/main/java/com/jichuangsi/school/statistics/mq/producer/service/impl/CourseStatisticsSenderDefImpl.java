/**
 * 
 */
package com.jichuangsi.school.statistics.mq.producer.service.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;
import com.jichuangsi.school.statistics.mq.producer.service.ICourseStatisticsSender;

/**
 * @author huangjiajun
 *
 */
@Service
public class CourseStatisticsSenderDefImpl implements ICourseStatisticsSender {

	@Resource
	private AmqpTemplate rabbitTemplate;

	@Value("${custom.mq.producer.queue-name.courseStatistics}")
	private String courseStatisticsMsgName;

	@Override
	public void send(CourseStatisticsModel courseStatiResponseModel) {
		rabbitTemplate.convertAndSend(courseStatisticsMsgName, JSONObject.toJSONString(courseStatiResponseModel));
	}

}
