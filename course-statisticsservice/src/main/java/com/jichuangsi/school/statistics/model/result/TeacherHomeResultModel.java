package com.jichuangsi.school.statistics.model.result;

import java.util.ArrayList;
import java.util.List;

public class TeacherHomeResultModel {

    private Integer studentNum;
    private Integer submitNum;
    private Integer subjectiveNum;
    private Integer objectiveNum;
    private List<QuestionResultModel> objective = new ArrayList<QuestionResultModel>();
    private List<QuestionResultModel> subjective = new ArrayList<QuestionResultModel>();

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

    public List<QuestionResultModel> getObjective() {
        return objective;
    }

    public void setObjective(List<QuestionResultModel> objective) {
        this.objective = objective;
    }

    public List<QuestionResultModel> getSubjective() {
        return subjective;
    }

    public void setSubjective(List<QuestionResultModel> subjective) {
        this.subjective = subjective;
    }
}
