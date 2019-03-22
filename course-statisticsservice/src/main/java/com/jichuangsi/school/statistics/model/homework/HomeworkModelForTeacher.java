package com.jichuangsi.school.statistics.model.homework;

import java.util.ArrayList;
import java.util.List;

public class HomeworkModelForTeacher extends HomeworkModel{
    private List<QuestionModelForTeacher> questions = new ArrayList<QuestionModelForTeacher>();
    private List<TransferStudent> students = new ArrayList<TransferStudent>();
    private int submitted;
    private int total;

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
