package com.jichuangsi.school.courseservice.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionForTeacher extends Question {

    List<AnswerForStudent> answerForStudent = new ArrayList<AnswerForStudent>();
    Statistics statistics;

    public List<AnswerForStudent> getAnswerForStudent() {
        return answerForStudent;
    }

    public void setAnswerForStudent(List<AnswerForStudent> answerForStudent) {
        this.answerForStudent = answerForStudent;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
