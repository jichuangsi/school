package com.jichuangsi.school.homeworkservice.repository;

import com.jichuangsi.school.homeworkservice.entity.Homework;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeworkRepository extends MongoRepository<Homework, String>, StudentHomeworkExtraRepository{

    Homework findFirstByIdOrderByUpdateTimeDesc(String homeworkId);

    Homework findByTeacherIdAndId(String teacherId,String homeId);
}
