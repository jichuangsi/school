package com.jichuangsi.school.eaxmservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_eaxm")
public class Eaxm {
    @Id
    private String eaxmId;
    private String eaxmName;
    private List<String> questionIds = new ArrayList<String>();
    private String teacherId;
    private String teacherName;
    long createTime;
    long updateTime;

    public String getEaxmId() {
        return eaxmId;
    }

    public void setEaxmId(String eaxmId) {
        this.eaxmId = eaxmId;
    }

    public String getEaxmName() {
        return eaxmName;
    }

    public void setEaxmName(String eaxmName) {
        this.eaxmName = eaxmName;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
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
}
