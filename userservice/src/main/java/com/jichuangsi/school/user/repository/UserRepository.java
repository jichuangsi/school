package com.jichuangsi.school.user.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jichuangsi.school.user.entity.UserInfo;

@Repository
public interface UserRepository extends MongoRepository<UserInfo, String>{
	UserInfo findOneByAccount(String account);
}
