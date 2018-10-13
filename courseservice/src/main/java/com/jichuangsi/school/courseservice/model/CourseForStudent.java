package com.jichuangsi.school.courseservice.model;

import java.util.ArrayList;
import java.util.List;

public class CourseForStudent extends Course{

    List<QuestionForStudent> questions = new ArrayList<QuestionForStudent>();

    public List<QuestionForStudent> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionForStudent> questions) {
        this.questions = questions;
    }

}
