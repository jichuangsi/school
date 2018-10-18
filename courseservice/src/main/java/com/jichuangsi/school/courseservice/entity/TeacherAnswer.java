package com.jichuangsi.school.courseservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "school_course_teacher_answer")
public class TeacherAnswer {

    @Id
    String id;
    String teacherId;
    String teacherName;
    String subjectivePic;
    String subjectivePicStub;
    Double subjectiveScore;
    String questionId;
    String studentAnswerId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getStudentAnswerId() {
        return studentAnswerId;
    }

    public void setStudentAnswerId(String studentAnswerId) {
        this.studentAnswerId = studentAnswerId;
    }
}
