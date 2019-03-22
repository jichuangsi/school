package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.SchoolInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISchoolInfoRepository extends MongoRepository<SchoolInfo,String> {

    SchoolInfo findByGradeIdsContaining(String gradeId);

    SchoolInfo findFirstById(String id);
}
