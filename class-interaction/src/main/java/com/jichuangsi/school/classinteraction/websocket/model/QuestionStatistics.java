/**
 * 题目统计信息
 */
package com.jichuangsi.school.classinteraction.websocket.model;

/**
 * @author huangjiajun
 *
 */
public class QuestionStatistics {
	private String courseId;
	private String questionId;
	private float accuracy;// 正确率
	private float score;// 分数
	private int count;// 提交人数

	public final String getCourseId() {
		return courseId;
	}

	public final void setCourseId(String courseId) {
		this.courseId = courseId;
	}

	public final String getQuestionId() {
		return questionId;
	}

	public final void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public final float getAccuracy() {
		return accuracy;
	}

	public final void setAccuracy(float accuracy) {
		this.accuracy = accuracy;
	}

	public final float getScore() {
		return score;
	}

	public final void setScore(float score) {
		this.score = score;
	}

	public final int getCount() {
		return count;
	}

	public final void setCount(int count) {
		this.count = count;
	}

}
