package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.SchoolInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISchoolInfoRepository extends MongoRepository<SchoolInfo,String> {

    SchoolInfo findByGradeIdsContaining(String gradeId);

    SchoolInfo findFirstById(String id);

    List<SchoolInfo> findByDeleteFlag(String delete);

    int countByDeleteFlagAndName(String delete,String name);

    SchoolInfo findFirstByDeleteFlagAndNameOrderByCreateTimeDesc(String delete , String name);
}
