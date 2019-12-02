package com.jichuangsi.school.homeworkservice.model;
import com.jichuangsi.school.homeworkservice.entity.Students;
import com.jichuangsi.school.homeworkservice.model.transfer.TransferStudent;

import java.util.ArrayList;
import java.util.List;

public class HomeworkModelForTeacher extends HomeworkModel{
    private List<QuestionModelForTeacher> questions = new ArrayList<QuestionModelForTeacher>();
    private List<TransferStudent> students = new ArrayList<TransferStudent>();
    private List<String> points = new ArrayList<String>();//记录分数的设置：第一个为单选题：第二个为多选题；第三个为主观题
    private int submitted;
    private int total;

    private List<Students> student=new ArrayList<Students>();

    public List<Students> getStudent() {
        return student;
    }

    public void setStudent(List<Students> student) {
        this.student = student;
    }

    public List<QuestionModelForTeacher> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionModelForTeacher> questions) {
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

    public int getSubmitted() {
        return submitted;
    }

    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
