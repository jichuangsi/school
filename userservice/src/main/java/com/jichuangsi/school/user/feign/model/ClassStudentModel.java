package com.jichuangsi.school.user.feign.model;

import com.jichuangsi.school.user.entity.UserInfo;
import com.jichuangsi.school.user.entity.org.ClassInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassStudentModel {
    private String classId;
    private String className;
    private List<UserInfo> student=new ArrayList<UserInfo>();

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public List<UserInfo> getStudent() {
        return student;
    }

    public void setStudent(List<UserInfo> student) {
        this.student = student;
    }

    public ClassStudentModel(){}

    public ClassStudentModel(String classId,String className,List<UserInfo> student){
        this.classId=classId;
        this.className=className;
        this.student=student;
    }
}
