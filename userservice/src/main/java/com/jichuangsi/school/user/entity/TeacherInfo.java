package com.jichuangsi.school.user.entity;

import java.util.ArrayList;
import java.util.List;

public class TeacherInfo extends RoleInfo {

    private School school;
    private Phrase phrase;
    private Class primaryClass;
    private List<Class> secondaryClasses = new ArrayList<Class>();
    private Subject primarySubject;
    private List<Subject> secondarySubjects = new ArrayList<Subject>();
    private Grade primaryGrade;
    private List<Grade> secondaryGrades = new ArrayList<Grade>();

    public School getSchool() {
        return school;
    }

    public void setSchool(String schoolId, String schoolName) {
        this.school = new School(schoolId, schoolName);
    }

    public void resetPhrase() {
        this.phrase = null;
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(String phraseId, String phraseName,String id) {
        this.phrase = new Phrase(phraseId, phraseName,id);
    }

    public void resetPrimaryClass() {
        this.primaryClass = null;
    }

    public Class getPrimaryClass() {
        return primaryClass;
    }

    public void setPrimaryClass(String classId, String className) {
        this.primaryClass = new Class(classId, className);
    }

    public List<Class> getSecondaryClasses() {
        return secondaryClasses;
    }

    public void addSecondaryClasses(String classId, String className) {
        this.secondaryClasses.add(new Class(classId, className));
    }

    public Subject getPrimarySubject() {
        return primarySubject;
    }

    public void setPrimarySubject(String subjectId, String subjectName) {
        this.primarySubject = new Subject(subjectId, subjectName);
    }

    public List<Subject> getSecondarySubjects() {
        return secondarySubjects;
    }

    public void addSecondarySubjects(String subjectId, String subjectName) {
        this.secondarySubjects.add(new Subject(subjectId, subjectName));
    }

    public Grade getPrimaryGrade() {
        return primaryGrade;
    }

    public void setPrimaryGrade(String gradeId, String gradeName) {
        this.primaryGrade = new Grade(gradeId, gradeName);
    }

    public List<Grade> getSecondaryGrades() {
        return secondaryGrades;
    }

    public void addSecondaryGrades(String gradeId, String gradeName) {
        this.secondaryGrades.add(new Grade(gradeId, gradeName));
    }

    public boolean equals(Object obj) {
        if (obj instanceof TeacherInfo) {
            return true;
        }
        return super.equals(obj);
    }

    public class Class {
        private String classId;
        private String className;

        public Class(String classId, String className){
            this.classId = classId;
            this.className = className;
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
    }

    public class Subject{
        private String subjectId;
        private String subjectName;

        public Subject(String subjectId, String subjectName){
            this.subjectId = subjectId;
            this.subjectName = subjectName;
        }

        public String getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(String subjectId) {
            this.subjectId = subjectId;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }
    }

    public class Grade{
        private String gradeId;
        private String gradeName;

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
    }

    public class Phrase {
        private String phraseId;
        private String phraseName;
        private String pid;

        public Phrase(String phraseId, String phraseName,String pid){
            this.phraseId = phraseId;
            this.phraseName = phraseName;
            this.pid = pid;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPhraseId() {
            return phraseId;
        }

        public void setPhraseId(String phraseId) {
            this.phraseId = phraseId;
        }

        public String getPhraseName() {
            return phraseName;
        }

        public void setPhraseName(String phraseName) {
            this.phraseName = phraseName;
        }
    }

    public class School {
        private String schoolId;
        private String schoolName;

        public School(String schoolId, String schoolName){
            this.schoolId = schoolId;
            this.schoolName = schoolName;
        }

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
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setPhrase(Phrase phrase) {
        this.phrase = phrase;
    }

    public void setPrimaryClass(Class primaryClass) {
        this.primaryClass = primaryClass;
    }

    public void setSecondaryClasses(List<Class> secondaryClasses) {
        this.secondaryClasses = secondaryClasses;
    }

    public void setPrimarySubject(Subject primarySubject) {
        this.primarySubject = primarySubject;
    }

    public void setSecondarySubjects(List<Subject> secondarySubjects) {
        this.secondarySubjects = secondarySubjects;
    }

    public void setPrimaryGrade(Grade primaryGrade) {
        this.primaryGrade = primaryGrade;
    }

    public void setSecondaryGrades(List<Grade> secondaryGrades) {
        this.secondaryGrades = secondaryGrades;
    }
}

