package com.jichuangsi.school.courseservice.service;

import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.model.transfer.TransferKnowledge;
import org.springframework.transaction.annotation.Transactional;

public interface IFeignClientService {

    @Transactional
    TransferKnowledge getKnowledgeOfParticularQuestion(String questionId) throws StudentCourseServiceException;
}

