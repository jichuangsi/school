package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.RoleInfo;
import com.jichuangsi.school.user.entity.StudentInfo;
import com.jichuangsi.school.user.entity.UserInfo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface IStudentInfoRepository extends MongoRepository<UserInfo,String> {
    UserInfo findFirstById(String id);
}
