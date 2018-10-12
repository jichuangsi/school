/**
 * 
 */
package com.jichuangsi.school.statistics.service.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.entity.CourseStatistics;
import com.jichuangsi.school.statistics.model.CourseStatiResponseModel;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;

/**
 * @author huangjiajun
 *
 */
@Service
public class CourseStatisticsServiceDefImpl implements ICourseStatisticsService{
	
	@Resource
    private AmqpTemplate rabbitTemplate;
	
	@Value("${custom.mq.sender.queue-name.courseStatistics}")
	private String courseStatisticsMsgName;

	@Override
	public CourseStatistics addToCourse(CourseStatistics courseStatistics) {
		//todo 判断课堂状态，未开始的课不能加入
		//todo保存学生参与课堂信息，已经加入的则不变，统计数据亦不变
		
		//获取统计信息
		CourseStatiResponseModel model = getCourseStatistics(courseStatistics.getCourseId());
		//发送课堂变化信息统计消息
		rabbitTemplate.convertAndSend(courseStatisticsMsgName, JSONObject.toJSONString(model));
		return courseStatistics;
	}

	@Override
	public CourseStatiResponseModel getCourseStatistics(String courseId) {
		CourseStatiResponseModel model = new CourseStatiResponseModel();
		model.setCourseId(courseId);
		model.setStudentCount(1);
		return model;
	}

}
