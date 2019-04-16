package com.jichuangsi.school.user.repository;

import com.jichuangsi.school.user.entity.backstage.TimeTableInfo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ITimeTableInfoRepository extends MongoRepository<TimeTableInfo,String> {

    TimeTableInfo findFirstByClassIdAndDeleteOrderByCreatedTimeDesc(String classId,String delete);

    TimeTableInfo findFirstByIdAndClassIdAndDelete(String id,String classId,String delete);

    int countByClassIdAndDelete(String classId,String delete);
}
