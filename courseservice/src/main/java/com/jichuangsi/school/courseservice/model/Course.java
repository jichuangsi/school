package com.jichuangsi.school.courseservice.model;

import com.jichuangsi.school.courseservice.constant.Status;

public class Course {
    String courseId;
    String courseName;
    String courseInfo;
    Status courseStatus;
    String teacherId;
    String teacherName;
    String classId;
    String className;
    long courseStartTime;
    long courseEndTime;
    long createTime;
    long updateTime;

    int pageNum;
    int pageSize;

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

    public String getCourseInfo() {
        return courseInfo;
    }

    public void setCourseInfo(String courseInfo) {
        this.courseInfo = courseInfo;
    }

    public Status getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(Status courseStatus) {
        this.courseStatus = courseStatus;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public long getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(long courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public long getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(long courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }
}
