/**
 * 
 */
package com.jichuangsi.school.statistics.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jichuangsi.school.statistics.entity.CourseStatisticsEntity;

/**
 * @author huangjiajun
 *
 */
@Repository
public interface CourseStatisticsRepository extends MongoRepository<CourseStatisticsEntity, String> {
	CourseStatisticsEntity findOneByCourseId(String courseId);
}
