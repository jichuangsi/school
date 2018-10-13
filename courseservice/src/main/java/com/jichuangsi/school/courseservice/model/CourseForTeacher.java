package com.jichuangsi.school.courseservice.model;

import java.util.ArrayList;
import java.util.List;

public class CourseForTeacher extends Course{
    List<QuestionForTeacher> questions = new ArrayList<QuestionForTeacher>();

    public List<QuestionForTeacher> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionForTeacher> questions) {
        this.questions = questions;
    }
}
