package com.jichuangsi.school.user.model.transfer;

import com.jichuangsi.school.user.model.org.ClassModel;
import com.jichuangsi.school.user.model.school.GradeModel;
import com.jichuangsi.school.user.model.school.SubjectModel;

import java.util.ArrayList;
import java.util.List;

public class TransferTeacher {
    private String teacherId;
    private String teacherName;
    private String phraseId;
    private String phraseName;
    private String subjectId;
    private String subjectName;
    private String gradeId;
    private String gradeName;
    private List<SubjectModel> secondarySubjects = new ArrayList<SubjectModel>();
    private String primaryClassId;
    private String primaryClassName;
    private List<ClassModel> secondaryClasses = new ArrayList<ClassModel>();
    private List<GradeModel> secondaryGrades = new ArrayList<GradeModel>();
    private String headMaster = "0";

    public String getHeadMaster() {
        return headMaster;
    }

    public void setHeadMaster(String headMaster) {
        this.headMaster = headMaster;
    }

    public List<SubjectModel> getSecondarySubjects() {
        return secondarySubjects;
    }

    public void setSecondarySubjects(List<SubjectModel> secondarySubjects) {
        this.secondarySubjects = secondarySubjects;
    }

    public String getPrimaryClassId() {
        return primaryClassId;
    }

    public void setPrimaryClassId(String primaryClassId) {
        this.primaryClassId = primaryClassId;
    }

    public String getPrimaryClassName() {
        return primaryClassName;
    }

    public void setPrimaryClassName(String primaryClassName) {
        this.primaryClassName = primaryClassName;
    }

    public List<ClassModel> getSecondaryClasses() {
        return secondaryClasses;
    }

    public void setSecondaryClasses(List<ClassModel> secondaryClasses) {
        this.secondaryClasses = secondaryClasses;
    }

    public List<GradeModel> getSecondaryGrades() {
        return secondaryGrades;
    }

    public void setSecondaryGrades(List<GradeModel> secondaryGrades) {
        this.secondaryGrades = secondaryGrades;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getPhraseId() {
        return phraseId;
    }

    public void setPhraseId(String phraseId) {
        this.phraseId = phraseId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPhraseName() {
        return phraseName;
    }

    public void setPhraseName(String phraseName) {
        this.phraseName = phraseName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
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
}
