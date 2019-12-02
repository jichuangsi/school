package com.jichuangsi.school.courseservice.model;

import java.util.ArrayList;
import java.util.List;

public class QuestionForTeacher extends Question {

    List<AnswerForStudent> answerForStudent = new ArrayList<AnswerForStudent>();
    Statistics statistics;
    List<String> student=new ArrayList<String>();

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }

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
