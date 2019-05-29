package com.jichuangsi.school.parents.feign.model;

import javax.validation.constraints.NotEmpty;

public class TestModelForStudent {
    private String testId;
    @NotEmpty
    private String testName;
    private String testInfo;
    private String teacherId;
    private String teacherName;
    private String classId;
    private String className;
    private long testPublishTime;
    private long testEndTime;
    private long createTime;
    private long updateTime;
    private String subjectName;
    private String subjectId;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestInfo() {
        return testInfo;
    }

    public void setTestInfo(String testInfo) {
        this.testInfo = testInfo;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getTestPublishTime() {
        return testPublishTime;
    }

    public void setTestPublishTime(long testPublishTime) {
        this.testPublishTime = testPublishTime;
    }

    public long getTestEndTime() {
        return testEndTime;
    }

    public void setTestEndTime(long testEndTime) {
        this.testEndTime = testEndTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }
}
