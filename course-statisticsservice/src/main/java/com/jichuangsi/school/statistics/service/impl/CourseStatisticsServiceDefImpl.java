/**
 * 
 */
package com.jichuangsi.school.statistics.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jichuangsi.school.statistics.model.AddToCourseModel;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;
import com.jichuangsi.school.statistics.model.QuestionStatisticsInfo;
import com.jichuangsi.school.statistics.model.QuestionStatisticsListModel;
import com.jichuangsi.school.statistics.model.StudentAnswerModel;
import com.jichuangsi.school.statistics.mq.producer.service.ICourseStatisticsSender;
import com.jichuangsi.school.statistics.mq.producer.service.IQuestionStatisticsSender;
import com.jichuangsi.school.statistics.service.ICourseStatisticsService;

/**
 * @author huangjiajun
 *
 */
@Service
public class CourseStatisticsServiceDefImpl implements ICourseStatisticsService {

	@Resource
	private ICourseStatisticsSender courseStatisticsSender;
	@Resource
	private IQuestionStatisticsSender questionStatisticsSender;

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

	@Override
	public QuestionStatisticsListModel getQuestionStatisticsList(String courseId) {
		
		return null;
	}

	@Override
	public StudentAnswerModel saveStudentAnswer(StudentAnswerModel answerModel) {
		//todo 更新作答信息
		
		QuestionStatisticsInfo info = getQuestionStatisticsInfo(answerModel.getCourseId(),answerModel.getQuestionId());
		//发送题目统计变更消息
		questionStatisticsSender.send(info);
		return null;
	}

	@Override
	public QuestionStatisticsInfo getQuestionStatisticsInfo(String courseId, String questionId) {
		// TODO Auto-generated method stub
		return null;
	}

}
