package com.jichuangsi.school.statistics.entity.performance.student;

import com.jichuangsi.school.statistics.entity.performance.student.BasePerformanceEntity;

public class CoursePerformanceEntity extends BasePerformanceEntity {

    private String courseId;
    private String courseName;

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
