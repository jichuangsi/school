/**
 * 题目统计信息
 */
package com.jichuangsi.school.statistics.model;

/**
 * @author huangjiajun
 *
 */
public class QuestionStatisticsInfo {
	private String questionId;
	private float score;// 分数
	private float acc;// 正确率
	private int count;// 作答人数

	public final String getQuestionId() {
		return questionId;
	}

	public final void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	public final float getScore() {
		return score;
	}

	public final void setScore(float score) {
		this.score = score;
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

}
