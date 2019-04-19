package com.jichuangsi.school.user.repository.parent;

import com.jichuangsi.school.user.entity.parent.ParentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IParentInfoRepository extends MongoRepository<ParentInfo,String> {

    ParentInfo findFirstByIdAndDeleteFlag(String id, String delete);

    ParentInfo findFirstByWeChatAndDeleteFlag(String weChat, String delete);

    int countByAccountAndDeleteFlag(String account, String delete);

    ParentInfo findFirstByAccountAndPwdAndDeleteFlag(String account, String pwd, String delete);

    List<ParentInfo> findByStudentIdsIn(List<String> studentIds);

    List<ParentInfo> findByStudentIdsContaining(String studentId);

    ParentInfo findFirstByStudentIdsContainingAndDeleteFlag(String studentId,String delete);
}
