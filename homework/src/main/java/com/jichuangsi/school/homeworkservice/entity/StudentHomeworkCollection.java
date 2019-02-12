package com.jichuangsi.school.homeworkservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_student_homework_collection")
public class StudentHomeworkCollection {

    @Id
    private String id;
    private String studentId;
    private String studentName;
    private List<HomeworkSummary> homeworks = new ArrayList<HomeworkSummary>();

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

    public List<HomeworkSummary> getHomeworks() {
        return homeworks;
    }

    public void setHomeworks(List<HomeworkSummary> homeworks) {
        this.homeworks = homeworks;
    }
}
