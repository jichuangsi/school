package com.jichuangsi.school.questionsrepository.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "school_question_statistics")
public class QuestionStatistics {
    @Id
    private String id;
    private String qidMD52;
    private Integer addPapercount;//组卷次数
    private Integer answerCount;//作答次数
    private Double average;//平均的得分率

    public QuestionStatistics(){}

    public QuestionStatistics(String qidMD52, Integer addPapercount, Integer answerCount, double average) {
        this.qidMD52 = qidMD52;
        this.addPapercount = addPapercount;
        this.answerCount = answerCount;
        this.average = average;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQidMD52() {
        return qidMD52;
    }

    public void setQidMD52(String qidMD52) {
        this.qidMD52 = qidMD52;
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
