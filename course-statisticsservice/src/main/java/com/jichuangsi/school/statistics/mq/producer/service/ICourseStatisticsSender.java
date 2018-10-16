/**
 * 发送课堂统计消息接口
 */
package com.jichuangsi.school.statistics.mq.producer.service;

import com.jichuangsi.school.statistics.model.CourseStatisticsModel;

/**
 * @author huangjiajun
 *
 */
public interface ICourseStatisticsSender {
	
	/**
	 * 发送课堂统计消息
	 */
	void send(CourseStatisticsModel courseStatiResponseModel);
	
}
