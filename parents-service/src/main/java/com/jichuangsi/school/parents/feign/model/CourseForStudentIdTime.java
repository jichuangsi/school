package com.jichuangsi.school.parents.feign.model;

import java.util.List;

public class CourseForStudentIdTime extends CourseForStudent {
    private String studentId;
    private List<Long> statisticsTimes;

    public List<Long> getStatisticsTimes() {
        return statisticsTimes;
    }

    public void setStatisticsTimes(List<Long> statisticsTimes) {
        this.statisticsTimes = statisticsTimes;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

}