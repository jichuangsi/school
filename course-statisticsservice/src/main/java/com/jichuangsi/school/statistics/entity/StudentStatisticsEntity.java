/**
 * 课程统计持久化实体
 */
package com.jichuangsi.school.statistics.entity;

import com.jichuangsi.school.statistics.entity.performance.student.CoursePerformanceEntity;
import com.jichuangsi.school.statistics.entity.performance.student.HomeworkPerformanceEntity;
import com.jichuangsi.school.statistics.entity.performance.student.TestPerformanceEntity;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author huangjiajun
 *
 */
@Document(collection = "student-statistics-info")
public class StudentStatisticsEntity {
	@Id
	private String uid;
	private String studentId;
	private String studentName;
	private long createdTime;
	private long updatedTime;

	private List<CoursePerformanceEntity> coursePerformance = new ArrayList<CoursePerformanceEntity>();
	private List<HomeworkPerformanceEntity> homeworkPerformance = new ArrayList<HomeworkPerformanceEntity>();
	private List<TestPerformanceEntity> testPerformance = new ArrayList<TestPerformanceEntity>();

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(long createdTime) {
		this.createdTime = createdTime;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public List<CoursePerformanceEntity> getCoursePerformance() {
		return coursePerformance;
	}

	public void setCoursePerformance(List<CoursePerformanceEntity> coursePerformance) {
		this.coursePerformance = coursePerformance;
	}

	public List<HomeworkPerformanceEntity> getHomeworkPerformance() {
		return homeworkPerformance;
	}

	public void setHomeworkPerformance(List<HomeworkPerformanceEntity> homeworkPerformance) {
		this.homeworkPerformance = homeworkPerformance;
	}

	public List<TestPerformanceEntity> getTestPerformance() {
		return testPerformance;
	}

	public void setTestPerformance(List<TestPerformanceEntity> testPerformance) {
		this.testPerformance = testPerformance;
	}
}
