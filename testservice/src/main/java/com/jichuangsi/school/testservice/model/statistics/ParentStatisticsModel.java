package com.jichuangsi.school.testservice.model.statistics;

import java.util.ArrayList;
import java.util.List;

public class ParentStatisticsModel {

    private String studentId;
    private String studentName;
    private List<Long> statisticsTimes = new ArrayList<Long>();
    private String subjectId;
    private String subjectName;
    private long beignTime;
    private String classId;
    private Integer studentNum;

    public Integer getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(Integer studentNum) {
        this.studentNum = studentNum;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public long getBeignTime() {
        return beignTime;
    }

    public void setBeignTime(long beignTime) {
        this.beignTime = beignTime;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<Long> getStatisticsTimes() {
        return statisticsTimes;
    }

    public void setStatisticsTimes(List<Long> statisticsTimes) {
        this.statisticsTimes = statisticsTimes;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
