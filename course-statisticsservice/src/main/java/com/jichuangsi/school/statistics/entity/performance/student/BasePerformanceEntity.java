package com.jichuangsi.school.statistics.entity.performance.student;

import java.util.ArrayList;
import java.util.List;

public abstract class BasePerformanceEntity {

    private String teacherId;
    private String teacherName;
    private int commend;
    private String remark;
    private List<QuestionPerformanceEntity> questions = new ArrayList<QuestionPerformanceEntity>();

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

    public int getCommend() {
        return commend;
    }

    public void setCommend(int commend) {
        this.commend = commend;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
