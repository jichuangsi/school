package com.jichuangsi.school.courseservice.model.feign.classType;

import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;

import java.util.ArrayList;
import java.util.List;

public class SearchStudentKnowledgeModel {

    private List<TransferStudent> transferStudents = new ArrayList<TransferStudent>();
    private List<String> questionIds = new ArrayList<String>();
    private String classId;

    public List<TransferStudent> getTransferStudents() {
        return transferStudents;
    }

    public void setTransferStudents(List<TransferStudent> transferStudents) {
        this.transferStudents = transferStudents;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }
}
