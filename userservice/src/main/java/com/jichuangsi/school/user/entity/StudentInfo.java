package com.jichuangsi.school.user.entity;

public class StudentInfo extends RoleInfo {

    private School school;
    private Phrase phrase;
    private Class primaryClass;
    private Grade primaryGrade;

    public School getSchool() {
        return school;
    }

    public void setSchool(String schoolId, String schoolName) {
        this.school = new School(schoolId, schoolName);
    }

    public Phrase getPhrase() {
        return phrase;
    }

    public void setPhrase(String phraseId, String phraseName,String id) {
        this.phrase = new Phrase(phraseId, phraseName,id);
    }

    public Class getPrimaryClass() {
        return primaryClass;
    }

    public void setPrimaryClass(String classId, String className) {
        this.primaryClass = new Class(classId, className);
    }

    public Grade getPrimaryGrade() {
        return primaryGrade;
    }

    public void setPrimaryGrade(String gradeId, String gradeName) {
        this.primaryGrade = new Grade(gradeId, gradeName);
    }

    public boolean equals(Object obj) {
        if (obj instanceof StudentInfo) {
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

    public void setPrimaryGrade(Grade primaryGrade) {
        this.primaryGrade = primaryGrade;
    }
}

