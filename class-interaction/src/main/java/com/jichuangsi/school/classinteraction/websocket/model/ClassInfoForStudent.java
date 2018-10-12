/**
 * 发送给学生的班级信息
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public class ClassInfoForStudent {

	public static final String TYPE_COURSE_START = "course-start";

	private String type;// 班级消息类型
	private String classId;// 班级ID
	private String courseId;// 课程ID

	public final String getType() {
		return type;
	}

	public final void setType(String type) {
		this.type = type;
	}

	public final String getClassId() {
		return classId;
	}

	public final void setClassId(String classId) {
		this.classId = classId;
	}

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

}
