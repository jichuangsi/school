package com.jichuangsi.school.eaxmservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.courseservice.model.transfer.TransferExam;
import com.jichuangsi.school.eaxmservice.Model.DeleteQueryModel;
import com.jichuangsi.school.eaxmservice.Model.EaxmModel;
import com.jichuangsi.school.eaxmservice.Model.QuestionModel;
import com.jichuangsi.school.eaxmservice.exception.EaxmException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IEaxmService {

    @Transactional
    void saveEaxm(UserInfoForToken userInfo, EaxmModel eaxmModel);

    @Transactional
    void deleteEaxms(DeleteQueryModel deleteQueryModel);

    @Transactional
    void updateEaxmQ(EaxmModel eaxmModel) throws EaxmException;

    @Transactional
    List<TransferExam> getTransferExamByTeacherId(String teacherId);

    @Transactional
    List<QuestionModel> getQuestions(String eaxmId);
}
