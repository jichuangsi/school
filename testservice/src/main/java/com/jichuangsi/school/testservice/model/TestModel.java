package com.jichuangsi.school.testservice.model;

import com.jichuangsi.school.testservice.constant.Status;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class TestModel {
    private String testId;
    @NotEmpty
    private String testName;
    private String testInfo;
    private Status testStatus;
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
    private List<Attachment> attachments = new ArrayList<Attachment>();

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

    public Status getTestStatus() {
        return testStatus;
    }

    public void setTestStatus(Status testStatus) {
        this.testStatus = testStatus;
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

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }
}
