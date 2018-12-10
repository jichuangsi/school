package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.entity.Exam;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IExamExtraRepository {

    List<Exam> findExamsByExamNameLike(String keyword,String pageSize,String pageIndex);

    long countByExamNameLike(String keyword);


}
