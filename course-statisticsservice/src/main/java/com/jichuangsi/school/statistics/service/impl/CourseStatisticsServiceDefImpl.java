/**
 * 
 */
package com.jichuangsi.school.statistics.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.CloseableIterator;
import org.springframework.stereotype.Service;

import com.jichuangsi.school.statistics.entity.CourseStatisticsEntity;
import com.jichuangsi.school.statistics.entity.QuestionAnswersEntity;
import com.jichuangsi.school.statistics.entity.QuestionStaticsticsEntity;
import com.jichuangsi.school.statistics.entity.StudentAddCourseEntity;
import com.jichuangsi.school.statistics.entity.StudentAnswerEntity;
import com.jichuangsi.school.statistics.model.AddToCourseModel;
import com.jichuangsi.school.statistics.model.CourseStatisticsModel;
import com.jichuangsi.school.statistics.model.QuestionStatisticsInfoModel;
import com.jichuangsi.school.statistics.model.QuestionStatisticsListModel;
import com.jichuangsi.school.statistics.model.StudentAnswerModel;
import com.jichuangsi.school.statistics.mq.producer.service.ICourseStatisticsSender;
import com.jichuangsi.school.statistics.mq.producer.service.IQuestionStatisticsSender;
import com.jichuangsi.school.statistics.repository.CourseStatisticsRepository;
import com.jichuangsi.school.statistics.repository.QuestionAnswersRepository;
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
	private QuestionAnswersRepository questionAnswersRepository;
	@Resource
	private MongoTemplate mongoTemplate;

	private static int count = 0;

	@Override
	public AddToCourseModel addToCourse(AddToCourseModel addToCourseModel) {
		final String courseId = addToCourseModel.getCourseId();
		final String userId = addToCourseModel.getUserId();

		synchronized (userId.intern()) {// 防止同一个并发用户重复加入，todo集群需要用分布式锁
			StudentAddCourseEntity studentAddCourseEntity = studentAddCourseRepository
					.findOneByUserIdAndCourseId(userId, courseId);
			if (null == studentAddCourseEntity) {
				studentAddCourseEntity = new StudentAddCourseEntity();
				studentAddCourseEntity.setCourseId(courseId);
				studentAddCourseEntity.setUserId(userId);
				studentAddCourseEntity.setUserName(addToCourseModel.getUserName());
				// 保存学生参与课堂信息
				studentAddCourseRepository.save(studentAddCourseEntity);
				// 更新统计信息，inc是原子性操作，并发处理时没问题。
				mongoTemplate.upsert(new Query(Criteria.where("courseId").is(courseId)),
						new Update().inc("studentCount", 1), CourseStatisticsEntity.class);

				// 获取统计信息
				CourseStatisticsModel model = getCourseStatistics(addToCourseModel.getCourseId());
				// 发送课堂变化信息统计消息
				courseStatisticsSender.send(model);
			}
		}

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
		final String courseId = answerModel.getCourseId();
		final String questionId = answerModel.getQuestionId();
		final String studentId = answerModel.getStudentId();

		StudentAnswerEntity studentAnswerEntity = answerModel.genStudentAnswerEntity();
		// 更新作答信息，考虑到同步问题，不能直接用Repository找然后更新，需要使用upsert
		Query query = new Query();
		query.addCriteria(Criteria.where("courseId").is(courseId).and("questionId").is(questionId)
				.and("studentAnswers.studentId").is(studentId));
		QuestionAnswersEntity questionAnswersEntity = mongoTemplate.findOne(query, QuestionAnswersEntity.class);
		Update update = new Update();
		if (null == questionAnswersEntity) {
			Query query1 = new Query();
			query1.addCriteria(Criteria.where("courseId").is(courseId).and("questionId").is(questionId));
			// addToSet存在则不加，不存在则加,push不管是否存在都加，这里用addToSet
			update.addToSet("studentAnswers", studentAnswerEntity);
			mongoTemplate.upsert(query1, update, QuestionAnswersEntity.class);
		} else {
			update.set("studentAnswers.$.score", studentAnswerEntity.getScore());
			update.set("studentAnswers.$.answer", studentAnswerEntity.getAnswer());
			update.set("studentAnswers.$.isRight", studentAnswerEntity.getIsRight());
			update.set("studentAnswers.$.quType", studentAnswerEntity.getQuType());
			mongoTemplate.updateFirst(query, update, QuestionAnswersEntity.class);
		}

		// 获取统计信息
		QuestionStatisticsInfoModel info = getQuestionStatisticsInfo(answerModel.getCourseId(),
				answerModel.getQuestionId());
		// 发送题目统计变更消息
		// questionStatisticsSender.send(info);
		return answerModel;
	}

	@Override
	public QuestionStatisticsInfoModel getQuestionStatisticsInfo(String courseId, String questionId) {
		QuestionStatisticsInfoModel info = null;
		info = new QuestionStatisticsInfoModel();
		info.setCourseId(courseId);
		info.setQuestionId(questionId);
		info.setAcc(0);
		info.setAvgScore(0);
		info.setCount(0);

		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("courseId").is(courseId).and("questionId").is(questionId)),
				Aggregation.unwind("studentAnswers"),
				Aggregation.group("studentAnswers.isRight").first("studentAnswers.isRight").as("rightFlag")
						.first("questionId").as("questionId").first("courseId").as("courseId")
						.sum("studentAnswers.score").as("totalScore").count().as("count"));//按正确与错误统计，最终统计出来是两条记录
		CloseableIterator<QuestionStatisticsInfoModel> aggRes = mongoTemplate.aggregateStream(aggregation,
				QuestionAnswersEntity.class, QuestionStatisticsInfoModel.class);

		if (null == aggRes) {
			return info;
		}
		QuestionStatisticsInfoModel temp;
		int rightCount = 0;// 正确计数
		while (aggRes.hasNext()) {
			temp = aggRes.next();
			info.setTotalScore(temp.getTotalScore() + info.getTotalScore());
			info.setCount(temp.getCount() + info.getCount());
			if (temp.isRightFlag()) {
				rightCount = temp.getCount();
			}
		}
		info.setAcc(rightCount / (float) info.getCount());
		info.setAvgScore(info.getTotalScore() / info.getCount());

		return info;
	}

}
