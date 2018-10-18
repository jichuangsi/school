package com.jichuangsi.school.courseservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "school_course_student_answer")
public class StudentAnswer {

    @Id
    String id;
    String studentId;
    String studentName;
    String objectiveAnswer;
    String subjectivePic;
    String subjectivePicStub;
    String objectiveResult;
    Double subjectiveScore;
    String questionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getObjectiveAnswer() {
        return objectiveAnswer;
    }

    public void setObjectiveAnswer(String objectiveAnswer) {
        this.objectiveAnswer = objectiveAnswer;
    }

    public String getSubjectivePic() {
        return subjectivePic;
    }

    public void setSubjectivePic(String subjectivePic) {
        this.subjectivePic = subjectivePic;
    }

    public String getSubjectivePicStub() {
        return subjectivePicStub;
    }

    public void setSubjectivePicStub(String subjectivePicStub) {
        this.subjectivePicStub = subjectivePicStub;
    }

    public String getObjectiveResult() {
        return objectiveResult;
    }

    public void setObjectiveResult(String objectiveResult) {
        this.objectiveResult = objectiveResult;
    }

    public Double getSubjectiveScore() {
        return subjectiveScore;
    }

    public void setSubjectiveScore(Double subjectiveScore) {
        this.subjectiveScore = subjectiveScore;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }
}
