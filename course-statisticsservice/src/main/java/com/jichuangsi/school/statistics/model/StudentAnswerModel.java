/**
 * 
 */
package com.jichuangsi.school.statistics.model;

import com.jichuangsi.school.statistics.entity.StudentAnswerEntity;

/**
 * @author huangjiajun
 *
 */
public class StudentAnswerModel {

	private String courseId;
	private String questionId;
	private float score;// 题目得分
	private boolean isRight;// 正确与否
	private String quType;// 题目类型，主观题，客观题
	private String answer;// 答案内容
	private String studentId;// 学生ID

	public StudentAnswerEntity genStudentAnswerEntity() {
		return new StudentAnswerEntity(score, isRight, quType, answer, studentId);
	}

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

	public final float getScore() {
		return score;
	}

	public final void setScore(float score) {
		this.score = score;
	}

	public final boolean getIsRight() {
		return isRight;
	}

	public final void setIsRight(boolean isRight) {
		this.isRight = isRight;
	}

	public final String getStudentId() {
		return studentId;
	}

	public final void setStudentId(String studentId) {
		this.studentId = studentId;
	}

}
