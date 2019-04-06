package com.jichuangsi.school.user.repository.backstage;

import com.jichuangsi.school.user.entity.backstage.BackRoleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBackRoleInfoRepository extends MongoRepository<BackRoleInfo,String> {

    int countByRoleNameAndDeleteFlag(String roleName, String delete);

    BackRoleInfo findFirstByIdAndDeleteFlag(String id, String delete);
}
