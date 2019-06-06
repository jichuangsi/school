package com.jichuangsi.school.courseservice.model;


import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;

import java.util.ArrayList;
import java.util.List;

public class CourseForTeacher extends Course{
    private List<QuestionForTeacher> questions = new ArrayList<QuestionForTeacher>();
    private List<TransferStudent> students = new ArrayList<TransferStudent>();
    private List<String> points = new ArrayList<String>();//记录分数的设置：第一个为单选题：第二个为多选题；第三个为主观题

    public List<QuestionForTeacher> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionForTeacher> questions) {
        this.questions = questions;
    }

    public List<TransferStudent> getStudents() {
        return students;
    }

    public void setStudents(List<TransferStudent> students) {
        this.students = students;
    }

    public List<String> getPoints() {
        return points;
    }

    public void setPoints(List<String> points) {
        this.points = points;
    }
}
