/**
 * 
 */
package com.jichuangsi.school.courseservice.model.message;

/**
 * @author huangjiajun
 *
 */
public class CourseMessageModel {
	private String classId;// 班级ID
	private String courseId;// 课程ID
	private String courseName;

	private String operId;// 操作用户ID
	private long timestamp;// 时间戳

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

	public final String getOperId() {
		return operId;
	}

	public final void setOperId(String operId) {
		this.operId = operId;
	}

	public final long getTimestamp() {
		return timestamp;
	}

	public final void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public final String getClassId() {
		return classId;
	}

	public final void setClassId(String classId) {
		this.classId = classId;
	}

	@Override
	public String toString(){
		StringBuffer objectInfo = new StringBuffer();
		objectInfo.append("{classId:" + classId + ",courseId:" + courseId + ",courseName:" + courseName+"}");
		return objectInfo.toString();
	}
}
