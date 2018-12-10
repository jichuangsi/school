package com.jichuangsi.school.examservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.examservice.Model.PageHolder;
import com.jichuangsi.school.examservice.Model.transfer.TransferExam;
import com.jichuangsi.school.examservice.Model.DeleteQueryModel;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.exception.ExamException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IExamService {
    @Transactional
    void saveExam(UserInfoForToken userInfo, ExamModel examModel);

    @Transactional
    void deleteExams(DeleteQueryModel deleteQueryModel);

    @Transactional
    void updateExamQ(ExamModel examModel) throws ExamException;

    @Transactional
    List<TransferExam> getTransferExamByTeacherId(String teacherId);

    @Transactional
    List<QuestionModel> getQuestions(String examId);

    @Transactional
    PageHolder<TransferExam> getExamByExamName(ExamModel examModel);
}
