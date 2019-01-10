package com.jichuangsi.school.courseservice.model;

import java.util.ArrayList;
import java.util.List;

public class IncorrectQuestionReturnModel {

    private String knowledgeId;
    private String knowledge;
    private int count;
    private List<QuestionForStudent> questions = new ArrayList<QuestionForStudent>();

    public String getKnowledgeId() {
        return knowledgeId;
    }

    public void setKnowledgeId(String knowledgeId) {
        this.knowledgeId = knowledgeId;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<QuestionForStudent> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionForStudent> questions) {
        this.questions = questions;
    }
}
