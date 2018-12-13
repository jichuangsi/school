package com.jichuangsi.school.examservice.repository;

import com.jichuangsi.school.examservice.Model.ExamModel;
import com.jichuangsi.school.examservice.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IQuestionExtraRepository {

    void deleteQuestionByExamId(String eid);

    List<Question> findPageQuestions(ExamModel examModel);


    long findCountByExamId(String eid);

    Question save(Question question);
}
