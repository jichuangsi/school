package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.GradeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IGradeInfoRepository extends MongoRepository<GradeInfo,String> {

    GradeInfo findByClassIdsContaining(String classId);

    GradeInfo findFirstById(String id);
}
