package com.jichuangsi.school.homeworkservice.model;

import com.jichuangsi.school.homeworkservice.model.transfer.TransferStudent;

import java.util.ArrayList;
import java.util.List;

public class HomeworkModelForTeacher extends HomeworkModel{
    private List<QuestionModelForTeacher> questions = new ArrayList<QuestionModelForTeacher>();
    private List<TransferStudent> students = new ArrayList<TransferStudent>();

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
}
