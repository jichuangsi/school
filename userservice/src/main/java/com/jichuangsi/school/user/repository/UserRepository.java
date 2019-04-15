package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends MongoRepository<UserInfo, String> {
    UserInfo findOneByAccount(String account);

    int countByAccount(String account);

    UserInfo findFirstById(String id);

    UserInfo findFirstByIdAndStatus(String id,String status);

    UserInfo findFirstByAccountAndStatus(String account,String status);

    List<UserInfo> findByIdIn(List<String> ids);
}
