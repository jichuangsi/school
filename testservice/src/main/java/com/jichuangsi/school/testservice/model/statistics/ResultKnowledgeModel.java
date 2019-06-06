package com.jichuangsi.school.testservice.model.statistics;

import com.jichuangsi.school.testservice.model.transfer.TransferKnowledge;

public class ResultKnowledgeModel {

    private String questionId;
    private TransferKnowledge transferKnowledge;
    private String courseId;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public TransferKnowledge getTransferKnowledge() {
        return transferKnowledge;
    }

    public void setTransferKnowledge(TransferKnowledge transferKnowledge) {
        this.transferKnowledge = transferKnowledge;
    }
}
