/**
 * 发送题目统计消息接口
 */
package com.jichuangsi.school.statistics.mq.producer.service;

import com.jichuangsi.school.statistics.model.QuestionStatisticsInfoModel;

/**
 * @author huangjiajun
 *
 */
public interface IQuestionStatisticsSender {
	/**
	 * 发送题目统计消息
	 */
	void send(QuestionStatisticsInfoModel questionStatisticsInfo);
}
