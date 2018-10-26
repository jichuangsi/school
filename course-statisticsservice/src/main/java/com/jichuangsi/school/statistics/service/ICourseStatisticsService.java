/**
 * 课堂统计服务
 */
package com.jichuangsi.school.statistics.service;

import com.jichuangsi.school.statistics.model.AddToCourseModel;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;
import com.jichuangsi.school.statistics.model.QuestionStatisticsInfoModel;
import com.jichuangsi.school.statistics.model.QuestionStatisticsListModel;
import com.jichuangsi.school.statistics.model.StudentAnswerModel;

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
	
	/**
	 *更新或保存学生作答信息
	 */
	StudentAnswerModel saveStudentAnswer(StudentAnswerModel answerModel);
	
	/**
	 * 获取课堂单个题目的统计数据
	 */
	QuestionStatisticsInfoModel getQuestionStatisticsInfo(String courseId,String questionId);
	
	/**
	 * 获取课堂中所有已发布题目的统计数据
	 */
	QuestionStatisticsListModel getQuestionStatisticsList(String courseId);
}
