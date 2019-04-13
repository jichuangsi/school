package com.jichuangsi.school.parents.repository;

import com.jichuangsi.school.parents.entity.GrowthDay;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface IGrowDayRepository extends MongoRepository<GrowthDay,String> {

    GrowthDay findFirstByIdAndStudentId(String id,String studentId);

    List<GrowthDay> findByStudentIdOrderByCreatedTime(String studentId);
}
