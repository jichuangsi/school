package com.jichuangsi.school.courseservice.model;

public class ReturnCourse {
    CourseForTeacher courseForTeacher;

    String beginTime;
    String gameOver;

    long distanceTime;

    public long getDistanceTime() {
        return distanceTime;
    }

    public void setDistanceTime(long distanceTime) {
        this.distanceTime = distanceTime;
    }

    public ReturnCourse() {
    }

    public CourseForTeacher getCourseForTeacher() {
        return courseForTeacher;
    }

    public void setCourseForTeacher(CourseForTeacher courseForTeacher) {
        this.courseForTeacher = courseForTeacher;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getGameOver() {
        return gameOver;
    }

    public void setGameOver(String gameOver) {
        this.gameOver = gameOver;
    }
}
