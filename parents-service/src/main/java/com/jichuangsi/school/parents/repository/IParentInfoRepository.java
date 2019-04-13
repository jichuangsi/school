package com.jichuangsi.school.parents.repository;

import com.jichuangsi.school.parents.entity.ParentInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IParentInfoRepository extends MongoRepository<ParentInfo,String> {

    ParentInfo findFirstById(String id);
}
