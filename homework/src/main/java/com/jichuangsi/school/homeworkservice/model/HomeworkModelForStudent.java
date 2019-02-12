package com.jichuangsi.school.homeworkservice.model;

import java.util.ArrayList;
import java.util.List;

public class HomeworkModelForStudent extends HomeworkModel{

    private List<QuestionModelForStudent> questions = new ArrayList<QuestionModelForStudent>();
    private boolean completed;

    public List<QuestionModelForStudent> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionModelForStudent> questions) {
        this.questions = questions;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
