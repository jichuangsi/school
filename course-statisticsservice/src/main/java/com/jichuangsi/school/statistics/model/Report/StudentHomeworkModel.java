package com.jichuangsi.school.statistics.model.Report;

import java.util.ArrayList;
import java.util.List;

public class StudentHomeworkModel {
    private String studentId;
    private String studentAccount;
    private String studentName;
    private List<HomeworkSummary> homeworks = new ArrayList<HomeworkSummary>();

    public List<HomeworkSummary> getHomeworks() {
        return homeworks;
    }

    public String getStudentAccount() {
        return studentAccount;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setHomeworks(List<HomeworkSummary> homeworks) {
        this.homeworks = homeworks;
    }

    public void setStudentAccount(String studentAccount) {
        this.studentAccount = studentAccount;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
