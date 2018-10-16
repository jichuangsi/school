/**
 * 课堂统计服务
 */
package com.jichuangsi.school.statistics.service;

import com.jichuangsi.school.statistics.model.AddToCourseModel;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;

/**
 * @author huangjiajun
 *
 */
public interface ICourseStatisticsService {
	
	/**
	 * 学生加入课堂
	 */
	AddToCourseModel addToCourse(AddToCourseModel addToCourseModel); 
	
	/**
	 * 获取课堂统计数据
	 */
	CourseStatisticsModel getCourseStatistics(String courseId);
}
