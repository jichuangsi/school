/**
 * 
 */
package com.jichuangsi.school.statistics.service.impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.jichuangsi.school.statistics.entity.CourseStatisticsEntity;
import com.jichuangsi.school.statistics.entity.StudentAddCourseEntity;
import com.jichuangsi.school.statistics.model.AddToCourseModel;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;
import com.jichuangsi.school.statistics.model.QuestionStatisticsInfo;
import com.jichuangsi.school.statistics.model.QuestionStatisticsListModel;
import com.jichuangsi.school.statistics.model.StudentAnswerModel;
import com.jichuangsi.school.statistics.mq.producer.service.ICourseStatisticsSender;
import com.jichuangsi.school.statistics.mq.producer.service.IQuestionStatisticsSender;
import com.jichuangsi.school.statistics.repository.CourseStatisticsRepository;
import com.jichuangsi.school.statistics.repository.StudentAddCourseRepository;
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
	@Resource
	private StudentAddCourseRepository studentAddCourseRepository;
	@Resource
	private CourseStatisticsRepository courseStatisticsRepository;
	@Resource
	private MongoTemplate mongoTemplate;

	@Override
	public AddToCourseModel addToCourse(AddToCourseModel addToCourseModel) {
		final String courseId = addToCourseModel.getCourseId();
		final String userId = addToCourseModel.getUserId();
		StudentAddCourseEntity studentAddCourseEntity = studentAddCourseRepository.findOneByUserIdAndCourseId(userId,
				courseId);
		if (null == studentAddCourseEntity) {
			studentAddCourseEntity = new StudentAddCourseEntity();
			studentAddCourseEntity.setCourseId(courseId);
			studentAddCourseEntity.setUserId(userId);
			studentAddCourseEntity.setUserName(addToCourseModel.getUserName());
			// 保存学生参与课堂信息
			studentAddCourseRepository.save(studentAddCourseEntity);
			// 更新统计信息
			mongoTemplate.upsert(new Query(Criteria.where("courseId").is(courseId)),
					new Update().inc("studentCount", 1), CourseStatisticsEntity.class);
		}

		// 获取统计信息
		CourseStatisticsModel model = getCourseStatistics(addToCourseModel.getCourseId());
		// 发送课堂变化信息统计消息
		courseStatisticsSender.send(model);
		return addToCourseModel;
	}

	@Override
	public CourseStatisticsModel getCourseStatistics(String courseId) {
		CourseStatisticsEntity entity = courseStatisticsRepository.findOneByCourseId(courseId);
		CourseStatisticsModel model = new CourseStatisticsModel();
		model.setCourseId(courseId);
		model.setStudentCount(null == entity ? 0 : entity.getCount());
		return model;
	}

	@Override
	public QuestionStatisticsListModel getQuestionStatisticsList(String courseId) {

		return null;
	}

	@Override
	public StudentAnswerModel saveStudentAnswer(StudentAnswerModel answerModel) {
		// todo 更新作答信息

		QuestionStatisticsInfo info = getQuestionStatisticsInfo(answerModel.getCourseId(), answerModel.getQuestionId());
		// 发送题目统计变更消息
		questionStatisticsSender.send(info);
		return null;
	}

	@Override
	public QuestionStatisticsInfo getQuestionStatisticsInfo(String courseId, String questionId) {

		return null;
	}

}
