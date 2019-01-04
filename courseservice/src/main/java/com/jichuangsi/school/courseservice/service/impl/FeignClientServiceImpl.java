package com.jichuangsi.school.courseservice.service.impl;

import com.jichuangsi.school.courseservice.Exception.StudentCourseServiceException;
import com.jichuangsi.school.courseservice.constant.ResultCode;
import com.jichuangsi.school.courseservice.entity.Question;
import com.jichuangsi.school.courseservice.model.transfer.TransferKnowledge;
import com.jichuangsi.school.courseservice.repository.QuestionRepository;
import com.jichuangsi.school.courseservice.service.IFeignClientService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Optional;

@Service
public class FeignClientServiceImpl implements IFeignClientService {
    @Resource
    private QuestionRepository questionRepository;

    @Override
    public TransferKnowledge getKnowledgeOfParticularQuestion(String questionId) throws StudentCourseServiceException {
        if(StringUtils.isEmpty(questionId)) throw new StudentCourseServiceException(ResultCode.PARAM_MISS_MSG);
        Optional<Question> result = questionRepository.findById(questionId);
        if(result.isPresent()){
            return new TransferKnowledge(result.get().getKnowledgeId(), result.get().getKnowledge());
        }
        throw new StudentCourseServiceException(ResultCode.QUESTION_NOT_EXISTED);
    }
}
