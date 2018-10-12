/**
 * 
 */
package com.jichuangsi.school.statistics.entity;

import java.util.Date;

/**
 * @author huangjiajun
 *
 */
public class CourseStatistics {

	private String uid;
	private String courseId;
	private String userId;
	private Date createdTime;

	public final String getUid() {
		return uid;
	}

	public final void setUid(String uid) {
		this.uid = uid;
	}

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

	public final Date getCreatedTime() {
		return createdTime;
	}

	public final void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

}
