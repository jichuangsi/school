package cn.com.jichuangsi.school.backstage.repository;

import cn.com.jichuangsi.school.backstage.entity.BackUserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBackUserInfoRepository extends MongoRepository<BackUserInfo,String> {

    int countByAccount(String account);

    BackUserInfo findFirstByAccountAndPwdAndStatus(String account,String pwd,String status);

    BackUserInfo findFirstByIdAndStatus(String id,String status);
}
