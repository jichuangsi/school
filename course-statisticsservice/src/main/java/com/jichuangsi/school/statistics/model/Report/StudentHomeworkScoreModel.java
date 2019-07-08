package com.jichuangsi.school.statistics.model.Report;

public class StudentHomeworkScoreModel {

    private String subjectName;
    private String homeworkName;
    private String className;
    private Double score;


    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }



    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public StudentHomeworkScoreModel(){}

    public StudentHomeworkScoreModel(String subjectName,String homeworkName,String className,Double score){
        this.className=className;
        this.subjectName=subjectName;
        this.homeworkName=homeworkName;
        this.score=score;
    }
}
