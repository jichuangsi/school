package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.Exam;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Repository
public interface IExamExtraRepository {


    @Transactional
    long countByExamNameLike(String userId, String keyword);
    @Transactional
    List<Exam> findExamByExamNameAndConditions(String userId, String keyword,Integer pageSize,Integer pageIndex);
    @Transactional
    Exam save(Exam exam);
    @Transactional
    void deleteExamsByExamIdIsIn(List<String> eids);
    @Transactional
    Exam findOneByExamId(String eid);
    @Transactional
    List<Exam> findByTeacherId(String tid);
    @Transactional
    List<Map<String,Object>> getGroupCount(String fields, String eid);

}
