package com.jichuangsi.school.parents.repository;

import com.jichuangsi.school.parents.entity.ParentMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IParentMessageRepository extends MongoRepository<ParentMessage,String> {

    int countByParentIdAndTeacherId(String parentId,String teacherId);
}
