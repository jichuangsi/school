/**
 * 课堂统计数据
 */
package com.jichuangsi.school.statistics.model;

/**
 * @author huangjiajun
 *
 */
public class CourseStatiResponseModel {
	private String courseId;
	private int studentCount;

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final int getStudentCount() {
		return studentCount;
	}

	public final void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

}
