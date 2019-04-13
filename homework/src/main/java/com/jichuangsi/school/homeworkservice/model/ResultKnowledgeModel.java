package com.jichuangsi.school.homeworkservice.model;

import com.jichuangsi.school.homeworkservice.model.transfer.TransferKnowledge;

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
