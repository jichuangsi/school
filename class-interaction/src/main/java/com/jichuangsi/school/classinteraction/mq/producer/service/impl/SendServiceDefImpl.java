/**
 * 
 */
package com.jichuangsi.school.classinteraction.mq.producer.service.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.classinteraction.model.AddToCourseModel;
import com.jichuangsi.school.classinteraction.mq.producer.service.ISendService;

/**
 * @author huangjiajun
 *
 */
@Service
public class SendServiceDefImpl implements ISendService {

	@Value("${custom.mq.producer.queue-name.course-studentAdd}")
	private String studentAdd;

	@Resource
	private AmqpTemplate rabbitTemplate;

	@Override
	public void snedAddToCourse(AddToCourseModel addToCourseModel) {
		rabbitTemplate.convertAndSend(studentAdd, JSONObject.toJSONString(addToCourseModel));
	}

}
