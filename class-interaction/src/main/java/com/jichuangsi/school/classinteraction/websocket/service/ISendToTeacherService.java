/**
 * 推送ws消息到老师端的服务接口
 */
package com.jichuangsi.school.classinteraction.websocket.service;

import com.jichuangsi.school.classinteraction.websocket.model.CourseStatistics;
import com.jichuangsi.school.classinteraction.websocket.model.QuestionStatistics;

/**
 * @author huangjiajun
 *
 */
public interface ISendToTeacherService {
	
	/**
	 * 推送课堂统计信息
	 */
	void sendCourseStatisticsInfo(CourseStatistics courseStatistics);
	
	/**
	 * 推送题目统计信息
	 */
	void sendQuestionStatisticsInfo(QuestionStatistics questionStatistics);

}
