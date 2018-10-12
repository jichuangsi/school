/**
 * 
 */
package com.jichuangsi.school.courseservice.service.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.courseservice.model.CourseInfoModel;
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
	private String startCourseMsgName;
	
	@Override
	public void startCourse(CourseInfoModel courseInfoModel) {
		//todo修改课程状态
		
		//发上课消息
		rabbitTemplate.convertAndSend(startCourseMsgName, JSONObject.toJSONString(courseInfoModel));
	}

}
