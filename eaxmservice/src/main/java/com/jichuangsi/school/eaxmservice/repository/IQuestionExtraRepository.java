package com.jichuangsi.school.eaxmservice.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface IQuestionExtraRepository {

    void deleteQuestionByEaxmId(String eid);
}
