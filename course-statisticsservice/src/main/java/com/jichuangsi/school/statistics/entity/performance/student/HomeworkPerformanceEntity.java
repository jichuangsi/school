package com.jichuangsi.school.statistics.entity.performance.student;

import com.jichuangsi.school.statistics.entity.performance.student.BasePerformanceEntity;

public class HomeworkPerformanceEntity extends BasePerformanceEntity {

    private String homeworkId;
    private String homeworkName;

    public String getHomeworkId() {
        return homeworkId;
    }

    public void setHomeworkId(String homeworkId) {
        this.homeworkId = homeworkId;
    }

    public String getHomeworkName() {
        return homeworkName;
    }

    public void setHomeworkName(String homeworkName) {
        this.homeworkName = homeworkName;
    }
}
