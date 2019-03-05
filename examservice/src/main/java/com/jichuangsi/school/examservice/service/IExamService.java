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
import java.util.Map;

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
    PageHolder<QuestionModel> getQuestions(ExamModel examModel);

    @Transactional
    List<QuestionModel> getQuestions(String examId);

    @Transactional
    PageHolder<ExamModel> getExamByExamName(UserInfoForToken userInfo, ExamModel examModel);

    @Transactional
    List<Map<String,Object>> getQuestionTypegroup(String eid);

    @Transactional
    List<Map<String,Object>> getQuestionDifficultgroup(String eid);
}
