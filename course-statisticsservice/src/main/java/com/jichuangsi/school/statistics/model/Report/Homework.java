package com.jichuangsi.school.statistics.model.Report;

import java.util.ArrayList;
import java.util.List;

public class Homework {
    private String subjectName;
    private String subjectId;
    private String homeworkId;
    private List<String> questionIds=new ArrayList<String>();

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public String getHomeworkId() {
        return homeworkId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
