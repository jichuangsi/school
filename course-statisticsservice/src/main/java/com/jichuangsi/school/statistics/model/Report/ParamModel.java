package com.jichuangsi.school.statistics.model.Report;

public class ParamModel {
   private String gradeId;
   private String subjectName;
   private long time;

    public String getSubjectName() {
        return subjectName;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
