package com.jichuangsi.school.statistics.model.homework;

import com.jichuangsi.school.statistics.model.homework.constant.Status;


import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

public class HomeworkModel {
    private String homeworkId;
    @NotEmpty
    private String homeworkName;
    private String homeworkInfo;
    private Status homeworkStatus;
    private String teacherId;
    private String teacherName;
    private String classId;
    private String className;
    private long homeworkPublishTime;
    private long homeworkEndTime;
    private long createTime;
    private long updateTime;
    private String subjectName;
    private String subjectId;
    private List<Attachment> attachments = new ArrayList<Attachment>();

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }

    public String getHomeworkInfo() {
        return homeworkInfo;
    }

    public void setHomeworkInfo(String homeworkInfo) {
        this.homeworkInfo = homeworkInfo;
    }

    public Status getHomeworkStatus() {
        return homeworkStatus;
    }

    public void setHomeworkStatus(Status homeworkStatus) {
        this.homeworkStatus = homeworkStatus;
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

    public long getHomeworkPublishTime() {
        return homeworkPublishTime;
    }

    public void setHomeworkPublishTime(long homeworkPublishTime) {
        this.homeworkPublishTime = homeworkPublishTime;
    }

    public long getHomeworkEndTime() {
        return homeworkEndTime;
    }

    public void setHomeworkEndTime(long homeworkEndTime) {
        this.homeworkEndTime = homeworkEndTime;
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
