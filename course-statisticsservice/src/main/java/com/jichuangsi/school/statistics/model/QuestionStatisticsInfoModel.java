/**
 * 题目统计信息
 */
package com.jichuangsi.school.statistics.model;

/**
 * @author huangjiajun
 *
 */
public class QuestionStatisticsInfoModel {
	private String questionId;
	private String courseId;
	private float avgScore;// 平均分
	private float acc;// 正确率
	private int count;// 作答人数

	private boolean rightFlag;// 统计是分开正确错误统计，但最终返回数据不需要这个属性
	private float totalScore;// 统计是分开正确错误统计，但最终返回数据不需要这个属性

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

	public final float getAvgScore() {
		return avgScore;
	}

	public final void setAvgScore(float avgScore) {
		this.avgScore = avgScore;
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

	public final boolean isRightFlag() {
		return rightFlag;
	}

	public final void setRightFlag(boolean rightFlag) {
		this.rightFlag = rightFlag;
	}

	public final float getTotalScore() {
		return totalScore;
	}

	public final void setTotalScore(float totalScore) {
		this.totalScore = totalScore;
	}

}
