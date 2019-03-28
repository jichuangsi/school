package com.jichuangsi.school.statistics.model.result;

import java.util.ArrayList;
import java.util.List;

public class TeacherHomeResultModel {

    private Integer studentNum;
    private Integer submitNum;
    private Integer subjectiveNum;
    private Integer objectiveNum;
    private List<QuestionResultModel> models = new ArrayList<QuestionResultModel>();

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public Integer getSubmitNum() {
        return submitNum;
    }

    public void setSubmitNum(Integer submitNum) {
        this.submitNum = submitNum;
    }

    public Integer getSubjectiveNum() {
        return subjectiveNum;
    }

    public void setSubjectiveNum(Integer subjectiveNum) {
        this.subjectiveNum = subjectiveNum;
    }

    public Integer getObjectiveNum() {
        return objectiveNum;
    }

    public void setObjectiveNum(Integer objectiveNum) {
        this.objectiveNum = objectiveNum;
    }

    public List<QuestionResultModel> getModels() {
        return models;
    }

    public void setModels(List<QuestionResultModel> models) {
        this.models = models;
    }
}
