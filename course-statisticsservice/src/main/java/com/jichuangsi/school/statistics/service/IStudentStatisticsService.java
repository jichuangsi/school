package com.jichuangsi.school.statistics.service;

import com.jichuangsi.school.statistics.model.result.performance.StudentCoursePerformanceMessageModel;


public interface IStudentStatisticsService {

	/**
	 * 学生课堂表现统计
	 */
	StudentCoursePerformanceMessageModel saveStudentCoursePerformance(StudentCoursePerformanceMessageModel performance);

}
