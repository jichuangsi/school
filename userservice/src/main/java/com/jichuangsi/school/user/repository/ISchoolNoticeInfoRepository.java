package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.backstage.SchoolNoticeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ISchoolNoticeInfoRepository extends MongoRepository<SchoolNoticeInfo,String> {
}
