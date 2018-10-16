/**
 * 
 */
package com.jichuangsi.school.statistics.model;

/**
 * @author huangjiajun
 *
 */
public class StudentAnswerModel {

	public static final String QUESTION_TYPE_SUBJECT = "subjective";
	public static final String QUESTION_TYPE_OBJECT = "objective";

	private String courseId;
	private String questionId;
	private float sorce;//题目得分
	private boolean isRight;//正确与否
	private String quType;// 题目类型，主观题，客观题
	private String answer;// 答案内容
	private String correctFromTeacher;//老师批改

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

	public final String getQuType() {
		return quType;
	}

	public final void setQuType(String quType) {
		this.quType = quType;
	}

	public final String getAnswer() {
		return answer;
	}

	public final void setAnswer(String answer) {
		this.answer = answer;
	}

	public final float getSorce() {
		return sorce;
	}

	public final void setSorce(float sorce) {
		this.sorce = sorce;
	}

	public final boolean isRight() {
		return isRight;
	}

	public final void setRight(boolean isRight) {
		this.isRight = isRight;
	}

	public final String getCorrectFromTeacher() {
		return correctFromTeacher;
	}

	public final void setCorrectFromTeacher(String correctFromTeacher) {
		this.correctFromTeacher = correctFromTeacher;
	}

}
