package com.jichuangsi.school.parents.repository;

import com.jichuangsi.school.parents.entity.ParentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IParentInfoRepository extends MongoRepository<ParentInfo,String> {

    ParentInfo findFirstByIdAndDeleteFlag(String id,String delete);

    ParentInfo findFirstByWeChatAndDeleteFlag(String weChat,String delete);

    int countByAccountAndDeleteFlag(String account,String delete);

    ParentInfo findFirstByAccountAndPwdAndDeleteFlag(String account,String pwd,String delete);

    List<ParentInfo> findByStudentIdsContaining(String studentId);

    ParentInfo findFirstByStudentIdsContainingAndDeleteFlag(String studentId,String delete);
}
