package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.SchoolRoleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISchoolRoleInfoRepository extends MongoRepository<SchoolRoleInfo,String> {

    int countByRoleNameAndDeleteFlagAndSchoolId(String roleName,String delete,String schoolId);

    SchoolRoleInfo findFirstByIdAndDeleteFlag(String id,String delete);

    List<SchoolRoleInfo> findByDeleteFlagAndSchoolId(String delete,String schoolId);

    List<SchoolRoleInfo> findByDeleteFlagAndIdIn(String delete,List<String> ids);
}
