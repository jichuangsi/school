package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.ClassInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IClassInfoRepository extends MongoRepository<ClassInfo,String> {

    List<ClassInfo> findByIdInAndDeleteFlag(List<String> ids,String delete);

    ClassInfo findFirstByDeleteFlagAndNameAndIdInOrderByCreateTimeDesc(String delete,String name,List<String> ids);

    ClassInfo findFirstByIdAndDeleteFlag(String id,String delete);
}
