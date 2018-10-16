/**
 * 
 */
package com.jichuangsi.school.statistics.service.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jichuangsi.school.statistics.model.AddToCourseModel;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;
import com.jichuangsi.school.statistics.mq.producer.service.ICourseStatisticsSender;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;

/**
 * @author huangjiajun
 *
 */
@Service
public class CourseStatisticsServiceDefImpl implements ICourseStatisticsService {

	@Resource
	private ICourseStatisticsSender courseStatisticsSender;

	@Override
	public AddToCourseModel addToCourse(AddToCourseModel addToCourseModel) {

		// todo保存学生参与课堂信息，已经加入的则不变，统计数据亦不变

		// 获取统计信息
		CourseStatisticsModel model = getCourseStatistics(addToCourseModel.getCourseId());
		// 发送课堂变化信息统计消息
		courseStatisticsSender.send(model);
		return addToCourseModel;
	}

	@Override
	public CourseStatisticsModel getCourseStatistics(String courseId) {
		CourseStatisticsModel model = new CourseStatisticsModel();
		model.setCourseId(courseId);
		model.setStudentCount(1);
		return model;
	}

}
