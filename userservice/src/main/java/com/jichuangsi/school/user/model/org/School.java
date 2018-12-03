package com.jichuangsi.school.user.model.org;

import java.util.ArrayList;
import java.util.List;

public class School {

    private String schoolId;
    private String schoolName;
    private String address;
    private long createTime;
    private long updateTime;

    public School(){}

    public School(String schoolId, String schoolName){
        this.schoolId = schoolId;
        this.schoolName = schoolName;
    }

    private List<Grade> grades = new ArrayList<Grade>();

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
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
