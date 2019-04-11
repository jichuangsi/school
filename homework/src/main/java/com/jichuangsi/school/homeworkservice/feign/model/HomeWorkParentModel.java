package com.jichuangsi.school.homeworkservice.feign.model;

public class HomeWorkParentModel {

    private String homeWorkId;
    private String homeWorkName;
    private long pulishTime;
    private long endTime;
    private String status;
    private String studentId;
    private String subjectName;

    public String getHomeWorkId() {
        return homeWorkId;
    }

    public void setHomeWorkId(String homeWorkId) {
        this.homeWorkId = homeWorkId;
    }

    public String getHomeWorkName() {
        return homeWorkName;
    }

    public void setHomeWorkName(String homeWorkName) {
        this.homeWorkName = homeWorkName;
    }

    public long getPulishTime() {
        return pulishTime;
    }

    public void setPulishTime(long pulishTime) {
        this.pulishTime = pulishTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
