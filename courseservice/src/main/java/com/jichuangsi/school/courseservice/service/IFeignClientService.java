package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import org.springframework.transaction.annotation.Transactional;

public interface IFeignClientService {

    @Transactional
    String getKnowledgeOfParticularQuestion(String questionId) throws StudentCourseServiceException;
}
