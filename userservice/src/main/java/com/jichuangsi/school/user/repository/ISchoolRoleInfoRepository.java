package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.SchoolRoleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ISchoolRoleInfoRepository extends MongoRepository<SchoolRoleInfo,String> {

    int countByRoleNameAndDeleteFlag(String roleName,String delete);

    SchoolRoleInfo findFirstByIdAndDeleteFlag(String id,String delete);

    List<SchoolRoleInfo> findByDeleteFlag(String delete);
}
