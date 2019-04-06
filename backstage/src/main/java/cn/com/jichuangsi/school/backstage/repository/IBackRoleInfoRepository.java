package cn.com.jichuangsi.school.backstage.repository;

import cn.com.jichuangsi.school.backstage.entity.BackRoleInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBackRoleInfoRepository extends MongoRepository<BackRoleInfo,String> {

    int countByRoleNameAndDeleteFlag(String roleName,String delete);

    BackRoleInfo findFirstByIdAndDeleteFlag(String id,String delete);
}
