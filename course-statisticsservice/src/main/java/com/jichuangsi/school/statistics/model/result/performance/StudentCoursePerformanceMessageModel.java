package com.jichuangsi.school.statistics.model.result.performance;

public class StudentCoursePerformanceMessageModel extends StudentPerformanceMessageModel {
	private String courseId;
	private String courseName;

	public StudentCoursePerformanceMessageModel(){
		msgType = "COURSE";
	}

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final String getCourseName() {
		return courseName;
	}

	public final void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	@Override
	public String toString(){
		StringBuffer objectInfo = new StringBuffer();
		objectInfo.append("{studentId:" + getStudentId()
				+ ",studentName:" + getStudentName()
				+ ",teacherId:" + getTeacherId()
				+",teacherName:" + getTeacherName()
				+ ",courseId:" + courseId
				+",courseName:" + courseName+"}");
		return objectInfo.toString();
	}
}
