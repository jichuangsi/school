package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.entity.DimensionQuestion;
import com.jichuangsi.school.examservice.entity.ExamDimension;
import com.jichuangsi.school.examservice.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionExtraRepository {

    void deleteQuestionByExamId(String eid);

    List<Question> findPageQuestions(ExamModel examModel);

    List<Question> findQuestionsByExamId(String examId);

    long findCountByExamId(String eid);

    Question save(Question question);

    ExamDimension save(ExamDimension question);

    List<DimensionQuestion> findDimensionQuestionByExamId(String examId);

    List<DimensionQuestion> findPageDimensionQuestion(ExamModel examModel);

    void deleteDimensionQuestionByExamId(String eid);

    long findCountByDimensionExamId(String eid);
}
