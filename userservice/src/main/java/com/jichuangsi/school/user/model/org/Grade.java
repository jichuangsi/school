package com.jichuangsi.school.user.model.org;

import java.util.ArrayList;
import java.util.List;

public class Grade {

    private String gradeId;
    private String gradeName;
    private List<Class> classes = new ArrayList<Class>();
    private long createTime;
    private long updateTime;

    public Grade(){}

    public Grade(String gradeId, String gradeName){
        this.gradeId = gradeId;
        this.gradeName = gradeName;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
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
