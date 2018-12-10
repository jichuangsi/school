package com.jichuangsi.school.examservice.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionExtraRepository {

    void deleteQuestionByExamId(String eid);
}
