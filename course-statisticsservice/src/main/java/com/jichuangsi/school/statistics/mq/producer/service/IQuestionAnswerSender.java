/**
 * 发送题目有学生作答信息接口
 */
package com.jichuangsi.school.statistics.mq.producer.service;

import com.jichuangsi.school.statistics.model.StudentAnswerModel;

/**
 * @author huangjiajun
 *
 */
public interface IQuestionAnswerSender {
	void send(StudentAnswerModel studentAnswerModel);
}
