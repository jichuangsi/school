package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.org.GradeInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IGradeInfoRepository extends MongoRepository<GradeInfo,String> {

    GradeInfo findByClassIdsContaining(String classId);

    GradeInfo findFirstById(String id);

    List<GradeInfo> findByDeleteFlagAndIdInOrderByCreateTime(String delete,List<String> ids);

    int countByDeleteFlagAndNameAndIdIn(String delete,String name,List<String> ids);

    GradeInfo findFirstByDeleteFlagAndNameAndIdInOrderByCreateTimeDesc(String delete,String name,List<String> ids);
}
