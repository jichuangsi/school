package com.jichuangsi.school.courseservice.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "school_QuesionStudent")
public class QuesionStudent {
    @Id
    private String id;
    private String quesionId;
    private String courseId;
    private List<String> student=new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuesionId() {
        return quesionId;
    }

    public void setQuesionId(String quesionId) {
        this.quesionId = quesionId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }
}
