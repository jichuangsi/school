package com.jichuangsi.school.courseservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "school_course_teacher_answer")
public class TeacherAnswer {

    @Id
    private String id;
    private String teacherId;
    private String teacherName;
    private String subjectivePic;
    private String subjectivePicStub;
    private Double subjectiveScore;
    private String questionId;
    private String studentAnswerId;
    private boolean isShare;
    private long createTime;
    private long updateTime;
    private long shareTime;

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

    public boolean isShare() {
        return isShare;
    }

    public void setShare(boolean share) {
        isShare = share;
    }

    public long getShareTime() {
        return shareTime;
    }

    public void setShareTime(long shareTime) {
        this.shareTime = shareTime;
    }
}
