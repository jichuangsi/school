package com.jichuangsi.school.examservice.service;

import com.jichuangsi.microservice.common.model.UserInfoForToken;
import com.jichuangsi.school.examservice.Model.DeleteQueryModel;
import com.jichuangsi.school.examservice.Model.Dimension.DimensionModel;
import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.Model.PageHolder;
import com.jichuangsi.school.examservice.Model.QuestionModel;
import com.jichuangsi.school.examservice.entity.ExamDimension;
import com.jichuangsi.school.examservice.entity.Question;
import com.jichuangsi.school.examservice.exception.ExamException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

public interface ExamDimensionSerivce {

    @Transactional
    void saveExam(UserInfoForToken userInfo, ExamModel examModel);

    @Transactional
    void deleteExams(DeleteQueryModel deleteQueryModel);

    @Transactional
    PageHolder<ExamModel> getExamByExamName(UserInfoForToken userInfo, ExamModel examModel);

    @Transactional
    PageHolder<QuestionModel> getQuestions(ExamModel examModel);

    @Transactional
    List<QuestionModel> getQuestions(String examId);

    @Transactional
    List<Map<String,Object>> getQuestionTypegroup(String eid);

    @Transactional
    List<Map<String,Object>> getQuestionDifficultgroup(String eid);

    /**
     * 查类型
     * @param userInfo
     * @return
     */
    @Transactional
    List<DimensionModel> getExamDimensionModel(UserInfoForToken userInfo) throws ExamException;
    /**
     * 根据类型id查试卷
     */
    @Transactional
    PageHolder<ExamModel> getExamDimensionById( UserInfoForToken userInfo,ExamModel examModel)throws ExamException;

    /**
     * 根据试卷id查题目信息
     * @param userInfo
     * @param examId
     * @return
     */
    @Transactional
    List<Question> getQuestionById(UserInfoForToken userInfo, @RequestParam String examId);
    /**
     * 生成组题试卷
     * @param
     * @param
     * @return
     */
    @Transactional
    void saveGroupQuestions(UserInfoForToken userInfoForToken,String examId);
}
