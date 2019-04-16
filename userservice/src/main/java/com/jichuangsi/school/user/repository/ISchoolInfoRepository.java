package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.SchoolInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISchoolInfoRepository extends MongoRepository<SchoolInfo,String> {

    SchoolInfo findByGradeIdsContaining(String gradeId);

    SchoolInfo findFirstById(String id);

    List<SchoolInfo> findByDeleteFlagOrderByCreateTime(String delete);

    int countByDeleteFlagAndName(String delete,String name);

    SchoolInfo findFirstByDeleteFlagAndNameOrderByCreateTimeDesc(String delete , String name);

    SchoolInfo findFirstByPhraseIdsContaining(String phraseId);

    SchoolInfo findFirstByDeleteFlagAndId(String delete,String id);

    int countByIdAndDeleteFlag(String schoolId,String delete);

    SchoolInfo findByGradeIdsContainingAndId(String gradeId,String id);
}
