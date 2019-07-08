package com.jichuangsi.school.statistics.model.Report;

import java.util.ArrayList;
import java.util.List;

public class TestModel {
    private String id;
    private String name;
    private String teacherId;
    private String teacherName;
    private String classId;
    private String className;
    private String subjectName;
    private String subjectId;
    private List<String> questionIds = new ArrayList<String>();

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId() {
        return classId;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<String> getQuestionIds() {
        return questionIds;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuestionIds(List<String> questionIds) {
        this.questionIds = questionIds;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}
