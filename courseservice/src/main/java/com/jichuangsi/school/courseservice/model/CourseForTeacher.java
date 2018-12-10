package com.jichuangsi.school.courseservice.model;

import com.jichuangsi.school.courseservice.model.transfer.TransferStudent;

import java.util.ArrayList;
import java.util.List;

public class CourseForTeacher extends Course{
    private List<QuestionForTeacher> questions = new ArrayList<QuestionForTeacher>();
    private List<TransferStudent> students = new ArrayList<TransferStudent>();

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
}
