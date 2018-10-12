/**
 * 客堂统计服务
 */
package com.jichuangsi.school.statistics.service;

import org.springframework.transaction.annotation.Transactional;

import com.jichuangsi.school.statistics.entity.CourseStatistics;
import com.jichuangsi.school.statistics.model.CourseStatiResponseModel;

/**
 * @author huangjiajun
 *
 */
public interface ICourseStatisticsService {
	
	/**
	 * 学生加入课堂
	 */
	CourseStatistics addToCourse(CourseStatistics courseStatistics); 
	
	/**
	 * 获取课堂统计数据
	 */
	CourseStatiResponseModel getCourseStatistics(String courseId);
}
