/**
 * 课程统计持久化实体
 */
package com.jichuangsi.school.statistics.entity;

import java.util.Date;

/**
 * @author huangjiajun
 *
 */
public class CourseStatisticsEntity {

	private String uid;
	private String courseId;
	private int count;
	private Date createdTime;
	private Date updatedTime;

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

	public final Date getCreatedTime() {
		return createdTime;
	}

	public final void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public final int getCount() {
		return count;
	}

	public final void setCount(int count) {
		this.count = count;
	}

	public final Date getUpdatedTime() {
		return updatedTime;
	}

	public final void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
