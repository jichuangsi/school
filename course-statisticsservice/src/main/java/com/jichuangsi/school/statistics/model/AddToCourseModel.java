/**
 * 
 */
package com.jichuangsi.school.statistics.model;

/**
 * @author huangjiajun
 *
 */
public class AddToCourseModel {
	private String courseId;
	private String userId;
	private String userName;

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final String getUserId() {
		return userId;
	}

	public final void setUserId(String userId) {
		this.userId = userId;
	}

	public final String getUserName() {
		return userName;
	}

	public final void setUserName(String userName) {
		this.userName = userName;
	}

}
