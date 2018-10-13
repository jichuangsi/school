/**
 *
 */
package com.jichuangsi.school.courseservice.service.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.courseservice.model.message.CourseMessageModel;
import com.jichuangsi.school.courseservice.service.ICourseService;

/**
 * @author huangjiajun
 *
 */

@Service
public class CourseServiceDefImpl implements ICourseService{

	@Resource
	private AmqpTemplate rabbitTemplate;

	@Value("${com.jichuangsi.school.mq.courses}")
	private String msgName;

	@Override
	public void startCourse(CourseMessageModel courseInfoModel) {
		//todo修改课程状态

		//发上课消息
		rabbitTemplate.convertAndSend(msgName, JSONObject.toJSONString(courseInfoModel));
	}

}
