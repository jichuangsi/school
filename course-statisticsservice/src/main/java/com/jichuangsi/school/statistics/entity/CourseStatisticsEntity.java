/**
 * 课程统计持久化实体
 */
package com.jichuangsi.school.statistics.entity;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author huangjiajun
 *
 */
@Document(collection = "course-statistics-info")
public class CourseStatisticsEntity {
	@Id
	private String uid;
	private String courseId;
	private int studentCount;
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

	public final int getStudentCount() {
		return studentCount;
	}

	public final void setStudentCount(int studentCount) {
		this.studentCount = studentCount;
	}

	public final Date getUpdatedTime() {
		return updatedTime;
	}

	public final void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
