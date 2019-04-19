package com.jichuangsi.school.statistics.entity.performance.student;

import com.jichuangsi.school.statistics.entity.performance.student.BasePerformanceEntity;

public class TestPerformanceEntity extends BasePerformanceEntity {

    private String testId;
    private String testName;

    public String getTestId() {
        return testId;
    }

    public void setTestId(String testId) {
        this.testId = testId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
