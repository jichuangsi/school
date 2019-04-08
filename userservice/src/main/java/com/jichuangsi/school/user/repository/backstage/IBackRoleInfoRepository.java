package com.jichuangsi.school.user.repository.backstage;

import com.jichuangsi.school.user.entity.backstage.BackRoleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IBackRoleInfoRepository extends MongoRepository<BackRoleInfo,String> {

    int countByRoleNameAndDeleteFlag(String roleName, String delete);

    BackRoleInfo findFirstByIdAndDeleteFlag(String id, String delete);

    List<BackRoleInfo> findByDeleteFlagOrderByCreatedTime(String delete);
}
