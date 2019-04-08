/**
 *
 */
package com.jichuangsi.school.statistics.repository;

import com.jichuangsi.school.statistics.entity.StudentAddCourseEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huangjiajun
 */
@Repository
public interface StudentAddCourseRepository extends MongoRepository<StudentAddCourseEntity, String> {

    StudentAddCourseEntity findOneByUserIdAndCourseId(String userId, String courseId);

    List<StudentAddCourseEntity> findByCourseId(String courseId);
}
