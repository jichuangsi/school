package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.SubjectInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISubjectInfoRepository extends MongoRepository<SubjectInfo,String> {

    List<SubjectInfo> findByDeleteFlagOrderByCreatedTime(String delete);

    int countByDeleteFlagAndSubjectName(String delete,String subjectName);

    SubjectInfo findFirstById(String id);

    SubjectInfo findFirstByIdAndDeleteFlag(String id,String delete);
}
