/**
 * 课程统计持久化实体
 */
package com.jichuangsi.school.statistics.entity;

import java.util.Date;

/**
 * @author huangjiajun
 *
 */
public class QuestionStaticsticsEntity {

	public static final String QUESTION_TYPE_SUBJECT = "subjective";
	public static final String QUESTION_TYPE_OBJECT = "objective";

	private String questionId;
	private String courseId;
	private String quType;// 题目类型，主观题，客观题
	private float avgScore;// 平均分
	private float acc;// 正确率
	private int count;// 作答人数

	private Date createdTime;
	private Date updatedTime;

	public final String getQuestionId() {
		return questionId;
	}

	public final void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final float getAcc() {
		return acc;
	}

	public final void setAcc(float acc) {
		this.acc = acc;
	}

	public final int getCount() {
		return count;
	}

	public final void setCount(int count) {
		this.count = count;
	}

	public final String getQuType() {
		return quType;
	}

	public final void setQuType(String quType) {
		this.quType = quType;
	}

	public final float getAvgScore() {
		return avgScore;
	}

	public final void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
	}

	public final Date getCreatedTime() {
		return createdTime;
	}

	public final void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public final Date getUpdatedTime() {
		return updatedTime;
	}

	public final void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

}
