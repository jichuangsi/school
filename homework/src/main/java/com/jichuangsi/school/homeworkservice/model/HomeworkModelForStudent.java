package com.jichuangsi.school.homeworkservice.model;

import java.util.ArrayList;
import java.util.List;

public class HomeworkModelForStudent extends HomeworkModel{

    List<QuestionModelForStudent> questions = new ArrayList<QuestionModelForStudent>();

    public List<QuestionModelForStudent> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionModelForStudent> questions) {
        this.questions = questions;
    }

}
