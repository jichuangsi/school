package com.jichuangsi.school.statistics.entity.performance.student;

import com.jichuangsi.school.statistics.entity.common.KnowledgeEntity;
import com.jichuangsi.school.statistics.entity.performance.student.BasePerformanceEntity;

import java.util.ArrayList;
import java.util.List;

public class QuestionPerformanceEntity extends BasePerformanceEntity {

    private String questionId;
    private String questionContent;
    private List<KnowledgeEntity> knowledges = new ArrayList<KnowledgeEntity>();

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public List<KnowledgeEntity> getKnowledges() {
        return knowledges;
    }

    public void setKnowledges(List<KnowledgeEntity> knowledges) {
        this.knowledges = knowledges;
    }
}
