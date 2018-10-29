/**
 * 
 */
package com.jichuangsi.school.statistics.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jichuangsi.school.statistics.entity.QuestionAnswersEntity;

/**
 * @author huangjiajun
 *
 */
@Repository
public interface QuestionAnswersRepository extends MongoRepository<QuestionAnswersEntity, String> {
	QuestionAnswersEntity findOneByCourseIdAndQuestionId(String courseId, String questionId);

	List<QuestionAnswersEntity> findByCourseId(String courseId);
}
