package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.backstage.SchoolNoticeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISchoolNoticeInfoRepository extends MongoRepository<SchoolNoticeInfo,String> {

    SchoolNoticeInfo findFirstByIdAndDeleteFlag(String id,String delete);

    List<SchoolNoticeInfo> findBySchoolIdAndDeleteFlagOrderByCreatedTimeDesc(String schoolId,String delete);

    SchoolNoticeInfo findFirstByIdAndDeleteFlagAndSchoolId(String id,String delete,String schoolId);

    int countBySchoolIdAndDeleteFlagOrderByCreatedTimeDesc(String schoolId,String delete);
}
