package com.jichuangsi.school.questionsrepository.model;

public class QuestionExtraNode {
    private QuestionNode questionNode;
    private Integer addPapercount;//组卷次数
    private Integer answerCount;//作答次数
    private Double average;//平均的得分率

    public QuestionNode getQuestionNode() {
        return questionNode;
    }

    public void setQuestionNode(QuestionNode questionNode) {
        this.questionNode = questionNode;
    }

    public Integer getAddPapercount() {
        return addPapercount;
    }

    public void setAddPapercount(Integer addPapercount) {
        this.addPapercount = addPapercount;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }
}
