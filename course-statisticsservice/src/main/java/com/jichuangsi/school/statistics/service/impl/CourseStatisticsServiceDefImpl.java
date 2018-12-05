/**
 * 
 */
package com.jichuangsi.school.statistics.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.jichuangsi.school.statistics.entity.CourseStatisticsEntity;
import com.jichuangsi.school.statistics.entity.QuestionAnswersEntity;
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

	@Override
	public AddToCourseModel addToCourse(AddToCourseModel addToCourseModel) {
		final String courseId = addToCourseModel.getCourseId();
		final String userId = addToCourseModel.getUserId();

		synchronized (userId.intern()) {// 防止同一个并发用户并发重复加入，todo集群需要用分布式锁
			StudentAddCourseEntity studentAddCourseEntity = studentAddCourseRepository
					.findOneByUserIdAndCourseId(userId, courseId);
			if (null == studentAddCourseEntity) {
				studentAddCourseEntity = new StudentAddCourseEntity();
				studentAddCourseEntity.setCourseId(courseId);
				studentAddCourseEntity.setUserId(userId);
				studentAddCourseEntity.setUserName(addToCourseModel.getUserName());
				studentAddCourseEntity.setCreatedTime(new Date());
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
		model.setStudentCount(null == entity ? 0 : entity.getStudentCount());
		return model;
	}

	@Override
	public QuestionStatisticsListModel getQuestionStatisticsList(String courseId) {
		List<QuestionAnswersEntity> entityList = questionAnswersRepository.findByCourseId(courseId);
		QuestionStatisticsListModel questionStatisticsListModel = new QuestionStatisticsListModel();
		List<QuestionStatisticsInfoModel> innerList = new ArrayList<QuestionStatisticsInfoModel>(entityList.size());
		QuestionStatisticsInfoModel model;
		for (QuestionAnswersEntity questionAnswersEntity : entityList) {
			model = new QuestionStatisticsInfoModel();
			fillQuestionStatisticsInfoModel(questionAnswersEntity, model);
			innerList.add(model);
		}
		questionStatisticsListModel.setCourseId(courseId);
		questionStatisticsListModel.setList(innerList);
		return questionStatisticsListModel;
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

		// 防止同一个并发用户并发重复加入，todo集群需要用分布式锁
		synchronized (studentId.intern()) {
			QuestionAnswersEntity questionAnswersEntity = mongoTemplate.findOne(query, QuestionAnswersEntity.class);
			Update update = new Update();
			if (null == questionAnswersEntity) {
				Query query1 = new Query();
				query1.addCriteria(Criteria.where("courseId").is(courseId).and("questionId").is(questionId));

				update.set("quType", answerModel.getQuType());
				update.inc("totalScore", answerModel.getScore());
				update.inc("count", 1);
				if (studentAnswerEntity.getIsRight()) {
					update.inc("accCount", 1);
				}
				// addToSet存在则不加，不存在则加,push不管是否存在都加，这里用addToSet
				update.addToSet("studentAnswers", studentAnswerEntity);
				mongoTemplate.upsert(query1, update, QuestionAnswersEntity.class);
			} else {
				StudentAnswerEntity oldstudentAnswerEntity = null;
				for (StudentAnswerEntity temp : questionAnswersEntity.getStudentAnswers()) {
					if (temp.getStudentId().equals(studentId)) {
						oldstudentAnswerEntity = temp;
						break;
					}
				}
				if (null != oldstudentAnswerEntity) {
					update.inc("totalScore", studentAnswerEntity.getScore() - oldstudentAnswerEntity.getScore());// 前后得分差
					if (!oldstudentAnswerEntity.getIsRight() && studentAnswerEntity.getIsRight()) {
						// 旧作答错误，新作答正确时
						update.inc("accCount", 1);
					}
					if (oldstudentAnswerEntity.getIsRight() && !studentAnswerEntity.getIsRight()) {
						// 旧作答正确，新作答错误时
						update.inc("accCount", -1);
					}
					update.set("studentAnswers.$.score", studentAnswerEntity.getScore());
					update.set("studentAnswers.$.answer", studentAnswerEntity.getAnswer());
					update.set("studentAnswers.$.isRight", studentAnswerEntity.getIsRight());
					mongoTemplate.updateFirst(query, update, QuestionAnswersEntity.class);
				}

			}

			// 获取统计信息
			QuestionStatisticsInfoModel info = getQuestionStatisticsInfo(answerModel.getCourseId(),
					answerModel.getQuestionId());
			// 发送题目统计变更消息
			questionStatisticsSender.send(info);
		}

		return answerModel;
	}

	@Override
	public QuestionStatisticsInfoModel getQuestionStatisticsInfo(String courseId, String questionId) {

		QuestionAnswersEntity entity = questionAnswersRepository.findOneByCourseIdAndQuestionId(courseId, questionId);
		QuestionStatisticsInfoModel info = new QuestionStatisticsInfoModel();
		if (null == entity) {
			info.setCourseId(courseId);
			info.setQuestionId(questionId);
			info.setAcc(0);
			info.setAvgScore(0);
			info.setCount(0);
			info.setMostError("");
		} else {
			fillQuestionStatisticsInfoModel(entity, info);
		}
		return info;
	}

	private void fillQuestionStatisticsInfoModel(QuestionAnswersEntity entity, QuestionStatisticsInfoModel info) {
		info.setCourseId(entity.getCourseId());
		info.setQuestionId(entity.getQuestionId());
		info.setAcc((float) entity.getAccCount() / entity.getCount());
		info.setAvgScore(entity.getTotalScore() / entity.getCount());
		info.setCount(entity.getCount());

		// 不是全部人答对
		if (entity.getAccCount() < entity.getCount()) {
			// 获取最多错误答案
			// 统计出各个错误答案的出现次数
			Map<String, Long> countMap = entity.getStudentAnswers().stream().filter(studentAnswer -> {
				if (studentAnswer.getIsRight()) {
					return false;
				} else {
					return true;
				}
			}).collect(Collectors.groupingBy(StudentAnswerEntity::getAnswer, Collectors.counting()));

			if (!countMap.isEmpty()) {
				// 找出出现次数最多的错误答案
				Optional<Entry<String, Long>> op = countMap.entrySet().stream().max((a1, a2) -> {
					return a1.getValue().intValue() - a2.getValue().intValue();
				});
				info.setMostError(op.get().getKey());
			}

		} else {
			info.setMostError("");
		}

	}

	public static void main(String[] args) {

	}

}
